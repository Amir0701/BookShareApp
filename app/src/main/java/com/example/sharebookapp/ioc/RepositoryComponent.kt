package com.example.sharebookapp.ioc

import com.example.sharebookapp.data.repository.Repository
import com.example.sharebookapp.data.repository.SingScope
import dagger.Component

@SingScope
@Component(modules = [ApiModule::class])
interface RepositoryComponent {
    fun getRepository(): Repository
}