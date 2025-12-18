package az.pashabank.starter.data.di

import az.pashabank.starter.data.local.card.CardDatabase
import az.pashabank.starter.data.local.card.CardLocalDataSource
import az.pashabank.starter.data.local.card.CardLocalDataSourceImpl
import az.pashabank.starter.data.local.customer.CustomerDatabase
import az.pashabank.starter.data.local.customer.CustomerLocalDataSource
import az.pashabank.starter.data.local.customer.CustomerLocalDataSourceImpl
import az.pashabank.starter.data.local.settings.AppSettingsDataSourceImpl
import az.pashabank.starter.data.local.settings.createDataStore
import az.pashabank.starter.data.local.transaction.TransactionDatabase
import az.pashabank.starter.data.local.transaction.TransactionLocalDataSource
import az.pashabank.starter.data.local.transaction.TransactionLocalDataSourceImpl
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import org.koin.dsl.module

val localModule = module {
    // Databases
    single {
        createRoomDatabase<CustomerDatabase>(
            "customer-db"
        ) {
            fallbackToDestructiveMigration(dropAllTables = false)
            build()
        }

    }

    single {
        createRoomDatabase<CardDatabase>(
            "card-db"
        ) {
            fallbackToDestructiveMigration(dropAllTables = false)
            build()
        }
    }

    single {
        createRoomDatabase<TransactionDatabase>(
            "transaction-db"
        ) {
            fallbackToDestructiveMigration(dropAllTables = false)
            build()
        }
    }

    // Local Data Sources
    single<CustomerLocalDataSource> { CustomerLocalDataSourceImpl(customerDao = get()) }
    single<CardLocalDataSource> { CardLocalDataSourceImpl(cardDao = get()) }
    single<TransactionLocalDataSource> { TransactionLocalDataSourceImpl(transactionDao = get()) }

    // DAOs
    single { get<CustomerDatabase>().customerDao() }
    single { get<CardDatabase>().cardDao() }
    single { get<TransactionDatabase>().transactionDao() }

    // Settings
    single { createDataStore() }
    factory<AppSettingsDataSource> {
        AppSettingsDataSourceImpl(
            dataStore = get()
        )
    }
}