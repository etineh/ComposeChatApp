package di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ktcompose.composechatapp.data.room.ChatDatabase
import com.ktcompose.composechatapp.data.room.dao.MessageDao
import com.ktcompose.composechatapp.data.room.dao.UserRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            app,
            ChatDatabase::class.java,
            "chat_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMessageDao(db: ChatDatabase): MessageDao = db.messageDao()

    @Provides
    fun provideUserRecordDao(db: ChatDatabase): UserRecordDao = db.userRecordDao()

}
