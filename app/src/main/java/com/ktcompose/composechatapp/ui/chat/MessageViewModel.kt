package com.ktcompose.composechatapp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.constants.MessageStatus
import com.ktcompose.composechatapp.data.model.MessageModel
import com.ktcompose.composechatapp.data.model.UserRecordModel
import com.ktcompose.composechatapp.data.repository.AppRepository
import com.ktcompose.composechatapp.data.repository.LocalStorageRepo
import com.ktcompose.composechatapp.utility.GenerateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: AppRepository,
    private val localStorageRepo: LocalStorageRepo
) : ViewModel() {

    // Observing user records
    val userRecords: StateFlow<List<UserRecordModel>> =
        repository.observeUserRecords()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // --- Send message state ---
    private val _sendMessageState = MutableStateFlow<Result<Unit>?>(null)
    val sendMessageState: StateFlow<Result<Unit>?> = _sendMessageState

    fun sendMessage(
        receiverUid: String,
        text: String
    ) {
        viewModelScope.launch {
            val tempId = UUID.randomUUID().toString() // temporary id until DB gives real one
            try {
                val sender = localStorageRepo.getMyUser() ?: return@launch

                val message = MessageModel(
                    text = text,
                    senderUid = sender.uid,
                    senderDisplayName = sender.displayName,
                    senderUsername = sender.username,
                    timeSent = System.currentTimeMillis(),
                    messageStatus = MessageStatus.SENT.name
                )

                // Optimistically add to UI
                _observerMessages[receiverUid]?.let { stateFlow ->
                    val current = stateFlow.value
                    stateFlow.value = current + message
                }

                val result = repository.sendMessage(
                    receiverUid = receiverUid,
                    messageModel = message
                )
                _sendMessageState.value = result


            } catch (e: Exception) {
                println("General log: failed to send")
                _observerMessages[receiverUid]?.let { stateFlow ->
                    val updated = stateFlow.value.map {
                        if (it.chatId == tempId) it.copy(messageStatus = MessageStatus.FAILED.name) else it
                    }
                    stateFlow.value = updated
                }
                _sendMessageState.value = Result.failure(e)
            }
        }
    }

    fun clearSendState() {
        _sendMessageState.value = null
    }


    // Messages flow
    private val _observerMessages = mutableMapOf<String, MutableStateFlow<List<MessageModel>>>()

    // Read-only map for external access
    val observerMessages: Map<String, StateFlow<List<MessageModel>>> = _observerMessages

    fun startObservingMessages(receiverUid: String) {
        if (_observerMessages.containsKey(receiverUid)) return

        val stateFlow = MutableStateFlow<List<MessageModel>>(emptyList())
        _observerMessages[receiverUid] = stateFlow

        val senderUid = K.getCurrentUserId() ?: return
        val conversationId = GenerateUtils.getConversationId(senderUid, receiverUid)

        // Load cached messages from ROOM first
        viewModelScope.launch {
            repository.getCachedMessages(conversationId).collect { cached ->
                val mapped = cached.map {
                    MessageModel(
                        chatId = it.chatId,
                        text = it.text,
                        senderUid = it.senderUid,
                        senderDisplayName = it.senderDisplayName,
                        senderUsername = it.senderUsername,
                        timeSent = it.timeSent,
                        messageStatus = it.messageStatus
                    )
                }
                stateFlow.value = mapped
            }
        }

        // load message from online database
        viewModelScope.launch {
            repository.observeMessages(receiverUid).collect { incoming ->
                val current = stateFlow.value

                // Save to ROOM
                repository.cacheMessages(conversationId, incoming)

                // Only add new messages
                val newOnes = incoming.filter { newMsg ->
                    current.none { it.chatId == newMsg.chatId }
                }

                if (newOnes.isNotEmpty()) {
                    stateFlow.value = current + newOnes
                }
            }
        }
    }



}