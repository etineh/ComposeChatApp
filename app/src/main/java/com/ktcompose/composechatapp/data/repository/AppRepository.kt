package com.ktcompose.composechatapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.constants.MessageStatus
import com.ktcompose.composechatapp.constants.MessageType
import com.ktcompose.composechatapp.data.model.MessageModel
import com.ktcompose.composechatapp.data.model.RegisterModel
import com.ktcompose.composechatapp.data.model.UserModel
import com.ktcompose.composechatapp.data.model.UserRecordModel
import com.ktcompose.composechatapp.data.room.dao.MessageDao
import com.ktcompose.composechatapp.data.room.dao.UserRecordDao
import com.ktcompose.composechatapp.data.room.entity.MessageEntity
import com.ktcompose.composechatapp.data.room.entity.UserRecordEntity
import com.ktcompose.composechatapp.extensions.isNullExt
import com.ktcompose.composechatapp.utility.CacheUtils
import com.ktcompose.composechatapp.utility.GenerateUtils
import com.ktcompose.composechatapp.utility.Mappers.toEntity
import com.ktcompose.composechatapp.utility.Mappers.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val realtimeDb: FirebaseDatabase,
    private val localStorageRepo: LocalStorageRepo,
    private val messageDao: MessageDao,
    private val userRecordDao: UserRecordDao
//    @ApplicationContext private val context: Context
) {

    //  AUTH --------------------------------------------------

    suspend fun registerUser(registerModel: RegisterModel): Result<UserRecordModel> {
        return try {
            // register the use
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                registerModel.email,
                registerModel.password
            ).await()

            // get the uid to check if registration is successful
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))

            val user = UserModel(
                uid = uid,
                displayName = registerModel.displayName,
                username = registerModel.username,
                email = registerModel.email,
                createdAt = System.currentTimeMillis()
            )

            val userRecord = UserRecordModel(
                uid = uid,
                displayName = registerModel.displayName,
                username = registerModel.username,
                lastMessage = "${registerModel.displayName} joined ComposeChat",
                lastMessageType = MessageType.TEXT,
                timestamp = System.currentTimeMillis(),
                unreadCount = 0,
                userIsOnline = true,
                messageStatus = MessageStatus.NONE,
                avatarColorHex = "#2E7D32"
            )

            // save user details to the user path on database
            realtimeDb.getReference(K.USERS)
                .child(uid)
                .setValue(user)

            // save user to localStorage ShareRef
            localStorageRepo.saveMyUser(user)

            // save user record to the user path on database
            realtimeDb.getReference(K.USERS_RECORD)
                .child(uid)
                .setValue(userRecord)
                .await()

            Result.success(userRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {

            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val myUid = K.getCurrentUserId()

            if (myUid.isNullExt()) return Result.failure(Exception("UID is null after login"))

            val user = getUser(myUid!!) ?: return Result.failure(Exception("User not found in database"))
            localStorageRepo.saveMyUser(user)    // save my details

            Result.success(Unit)
        } catch (e: Exception) {
            println("General log: fail login ${e.message}")

            Result.failure(e)
        }
    }


    //  USERS --------------------------------------------------

    private suspend fun getUser(uid: String): UserModel? {
        return try {
            val snapshot = realtimeDb.getReference(K.USERS).child(uid).get().await()
            snapshot.getValue(UserModel::class.java)
        } catch (e: Exception) {
            println("General log: error getting user ${e.message}")
            null
        }
    }


//    suspend fun getAllUsers(): List<UserModel>? {
//        return try {
//            val snapshot = realtimeDb.getReference(K.USERS).get().await()
//            snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
//        } catch (e: Exception) {
//            println("General log: Error getting all user  ${e.message}")
//            null
//        }
//    }


    fun observeUserRecords(): Flow<List<UserRecordModel>> = channelFlow {
        // 1. Emit cached Room data first
        launch(Dispatchers.IO) {
            userRecordDao.observeUserRecords().collect { entities ->
                send(entities.map { it.toModel() }.sortedByDescending { it.timestamp })
            }
        }

        // 2. Listen to Firebase in parallel
        val ref = realtimeDb.getReference(K.USERS_RECORD)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUid = K.getCurrentUserId()
                val users = snapshot.children
                    .mapNotNull { it.getValue(UserRecordModel::class.java) }
                    .filter { it.uid != currentUid }

                // Save to Room (will also auto-update flow above)
                CoroutineScope(Dispatchers.IO).launch {
                    userRecordDao.clearAll()
                    userRecordDao.insertAll(users.map { it.toEntity() })
                }

                trySend(users.sortedByDescending { it.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }



//    fun observeUserRecords(): Flow<List<UserRecordModel>> = callbackFlow {
//        val ref = realtimeDb.getReference(K.USERS_RECORD)
//
//        val listener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val currentUid = K.getCurrentUserId() // get my uid
//                val users = snapshot.children
//                    .mapNotNull { it.getValue(UserRecordModel::class.java) }
//                    .filter { it.uid != currentUid } // exclude myself
//
//                CacheUtils.saveUsersRecord(users)   // save to in-memory cache
//
//                trySend(users.sortedByDescending { it.timestamp }) // emit the updated list
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                close(error.toException())
//            }
//        }
//
//
//        ref.addValueEventListener(listener)
//
//        awaitClose { ref.removeEventListener(listener) }
//    }


    // MESSAGES --------------------------------------------------

    suspend fun sendMessage(receiverUid: String, messageModel: MessageModel): Result<Unit> {
        return try {
            val senderUid = K.getCurrentUserId() ?: return Result.failure(Exception("Sender UID null"))

            val conversationId = GenerateUtils.getConversationId(senderUid, receiverUid)

            // Generate messageId
            val messageId = realtimeDb.getReference(K.MESSAGES).child(conversationId)
                .push().key ?: return Result.failure(Exception("Message ID null"))

            messageModel.chatId = messageId

            // Save message
            realtimeDb.getReference(K.MESSAGES)
                .child(conversationId)
                .child(messageId)
                .setValue(messageModel)
                .await()

            val updates = mapOf(
                "lastMessage" to (messageModel.text ?: ""),
                "timestamp" to System.currentTimeMillis(),
//                "userIsOnline" to true
            )

            // Update sender record
            realtimeDb.getReference(K.USERS_RECORD)
                .child(senderUid)
                .updateChildren(updates)
                .await()

            // Update receiver record
            realtimeDb.getReference(K.USERS_RECORD)
                .child(receiverUid)
                .updateChildren(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeMessages(receiverUid: String): Flow<List<MessageModel>> = callbackFlow {
        val senderUid = firebaseAuth.currentUser?.uid
        if (senderUid == null) {
            close(Exception("User not logged in"))
            return@callbackFlow
        }

        val conversationId = GenerateUtils.getConversationId(senderUid, receiverUid)

        val ref = realtimeDb.getReference(K.MESSAGES)
            .child(conversationId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
//                    println("General log: message is $it")
                    it.getValue(MessageModel::class.java)
                }
                trySend(messages) // latest first
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose { ref.removeEventListener(listener) }
    }

    // ======== Room for offline cache
    suspend fun cacheMessages(conversationId: String, messages: List<MessageModel>) {
        val entities = messages.map {
            MessageEntity(
                chatId = it.chatId ?: UUID.randomUUID().toString(),
                conversationId = conversationId,
                text = it.text,
                senderUid = it.senderUid ?: "",
                senderDisplayName = it.senderDisplayName,
                senderUsername = it.senderUsername,
                timeSent = it.timeSent,
                messageStatus = it.messageStatus
            )
        }
        messageDao.insertMessages(entities)
    }

    fun getCachedMessages(conversationId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessages(conversationId)
    }

}
