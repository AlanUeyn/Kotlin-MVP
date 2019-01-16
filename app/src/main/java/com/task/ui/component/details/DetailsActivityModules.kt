package com.task.ui.component.details

import androidx.lifecycle.ViewModel
import com.task.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class DetailsActivityModules {
    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
   internal abstract fun bindSplashViewModel(viewModel: DetailsViewModel): ViewModel
}
