package com.example.rxtracker.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rxtracker.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMedicationDao(db: AppDatabase): MedicationDao {
        return db.medicationDao()
    }

    @Provides
    @Singleton
    fun provideScheduledDoseDao(db: AppDatabase) : ScheduledDoseDao {
        return db.scheduledDoseDao()
    }

    @Provides
    @Singleton
    fun provideDoseTakenDao(db: AppDatabase) : DoseTakenDao {
        return db.doseTakenDao()
    }
}