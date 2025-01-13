package ru.glebik.tinkoff_fintech.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ru.glebik.core.db.api.dao.MessageDao
import ru.glebik.core.db.api.dao.ReactionDao
import ru.glebik.core.db.api.dao.StreamDao
import ru.glebik.core.db.api.dao.TopicDao
import ru.glebik.core.db.di.DatabaseModule
import ru.glebik.core.navigation.di.NavigationModule
import ru.glebik.core.presentation.di.PresentationModule
import ru.glebik.core.utils.di.DefaultDispatcherQualifier
import ru.glebik.core.utils.di.DispatchersModule
import ru.glebik.core.utils.di.IoDispatcherQualifier
import ru.glebik.core.utils.di.MainDispatcherQualifier
import ru.glebik.tinkoff_fintech.feature.user.di.UserModule
import ru.glebik.tinkoff_fintech.feature.user.domain.GetAllUsersUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.GetOwnUserUseCase
import ru.glebik.tinkoff_fintech.feature.user.domain.SearchUsersUseCase
import ru.glebik.tinkoff_fintech.main.di.AppComponent
import ru.glebik.tinkoff_fintech.test.ChatFragmentTest
import javax.inject.Singleton


@Component(
    modules = [NavigationModule::class, TestNetworkModule::class,
        DispatchersModule::class, PresentationModule::class,
        DatabaseModule::class, UserModule::class]
)
@Singleton
interface TestAppComponent : AppComponent {

    override fun retrofit(): Retrofit

    //navigation
    override fun router(): Router

    //presentation
    override fun storeFactory(): StoreFactory

    //dispatchers
    @IoDispatcherQualifier
    override fun ioDispatcher(): CoroutineDispatcher

    @DefaultDispatcherQualifier
    override fun defaultDispatcher(): CoroutineDispatcher

    @MainDispatcherQualifier
    override fun mainDispatcher(): CoroutineDispatcher

    //db
    override fun topicDao(): TopicDao

    override fun streamDao(): StreamDao

    override fun messageDao(): MessageDao

    override fun reactionDao(): ReactionDao

    //user
    override fun getAllUsersUseCase(): GetAllUsersUseCase

    override fun searchUsersUseCase(): SearchUsersUseCase

    override fun getOwnUserUseCase(): GetOwnUserUseCase

    fun inject(chatFragmentTest: ChatFragmentTest)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context,
        ): TestAppComponent
    }
}