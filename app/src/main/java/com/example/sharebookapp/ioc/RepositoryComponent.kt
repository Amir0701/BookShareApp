package com.example.sharebookapp.ioc

import com.example.sharebookapp.data.repository.Repository
import dagger.Component

@Component
interface RepositoryComponent {
    fun getRepository(): Repository
}