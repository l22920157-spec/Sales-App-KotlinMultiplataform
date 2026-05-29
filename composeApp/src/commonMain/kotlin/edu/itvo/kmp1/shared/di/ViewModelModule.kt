package edu.itvo.kmp1.shared.di

import edu.itvo.kmp1.shared.presentation.viewmodel.CustomerFormViewModel
import edu.itvo.kmp1.shared.presentation.viewmodel.CustomerViewModel
import edu.itvo.kmp1.shared.presentation.viewmodel.ProductFormViewModel
import edu.itvo.kmp1.shared.presentation.viewmodel.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CustomerViewModel(get(), get()) }
    viewModel { ProductViewModel(get(), get()) }
    viewModel { CustomerFormViewModel(get(), get(), get()) }
    viewModel { ProductFormViewModel(get(), get(), get()) }
}
