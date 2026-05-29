package edu.itvo.kmp1.shared.di

import edu.itvo.kmp1.shared.domain.usecase.customer.CreateCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.DeleteCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.FindCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.ListCustomerUseCase
import edu.itvo.kmp1.shared.domain.usecase.customer.UpdateCustomerCase
import edu.itvo.kmp1.shared.domain.usecase.product.CreateProductUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.DeleteProductUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.FindProductByCodeUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.ListProductsUseCase
import edu.itvo.kmp1.shared.domain.usecase.product.UpdateProductUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { CreateCustomerUseCase(get()) }
    factory { UpdateCustomerCase(get()) }
    factory { DeleteCustomerUseCase(get()) }
    factory { FindCustomerUseCase(get()) }
    factory { ListCustomerUseCase(get()) }

    factory { CreateProductUseCase(get()) }
    factory { UpdateProductUseCase(get()) }
    factory { DeleteProductUseCase(get()) }
    factory { FindProductByCodeUseCase(get()) }
    factory { ListProductsUseCase(get()) }
}