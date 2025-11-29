package com.example.myapplication.di

import com.example.myapplication.features.dollar.data.database.dao.IDollarDao
import com.example.myapplication.features.dollar.data.datasource.DollarLocalDataSource
import com.example.myapplication.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.myapplication.features.dollar.data.repository.DollarRepository
import com.example.myapplication.features.dollar.domain.repository.IDollarRepository
import com.example.myapplication.features.dollar.domain.usecases.FetchDollarUseCase
import com.example.myapplication.features.dollar.presentation.DollarViewModel
import com.example.myapplication.features.github.data.api.GithubService
import com.example.myapplication.features.github.data.datasourse.GithubRemoteDataSource
import com.example.myapplication.features.github.data.repository.GithubRepository
import com.example.myapplication.features.github.domain.repository.IgithubRepository
import com.example.myapplication.features.github.domain.usercases.FindByNickNameUseCase
import com.example.myapplication.features.github.presentation.GirhubViewModel
import com.example.myapplication.features.login.data.repository.LoginRepository
import com.example.myapplication.features.login.domain.repository.ILoginRepository
import com.example.myapplication.features.login.domain.usercases.LoginUseCase
import com.example.myapplication.features.login.presentation.LogInViewModel
import com.example.myapplication.features.maintenance.data.MaintenanceDataStore
import com.example.myapplication.features.maintenance.data.MaintenanceRepository
import com.example.myapplication.features.maintenance.presentation.MaintenanceViewModel
import com.example.myapplication.features.movies.data.api.MovieService
import com.example.myapplication.features.movies.data.database.AppRoomDatabase
import com.example.myapplication.features.movies.data.database.dao.IMovieDao
//import com.example.myapplication.features.movies.data.database.AppRoomDatabaseMovies
import com.example.myapplication.features.movies.data.datasource.MovieLocalDataSource
import com.example.myapplication.features.movies.data.datasource.MovieRemoteDataSource
import com.example.myapplication.features.movies.data.repository.MovieRepository
import com.example.myapplication.features.movies.domain.repository.IMovieRepository
import com.example.myapplication.features.movies.domain.usercases.GetMoviesUseCase
import com.example.myapplication.features.movies.presentation.MoviesViewModel
import com.example.myapplication.features.profile.data.repository.ProfileRepository
import com.example.myapplication.features.profile.domain.repository.IProfileRepository
import com.example.myapplication.features.profile.domain.usercases.GetProfileUseCase
import com.example.myapplication.features.profile.presentation.ProfileViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module() {
    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single(named("github")) {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single(named("movies")){
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // GithubService
    single<GithubService> {
        get<Retrofit>(named("github")).create(GithubService::class.java)
    }
    single{ GithubRemoteDataSource(get()) }
    single< IgithubRepository>{ GithubRepository(get()) }

    factory { FindByNickNameUseCase(get()) }
    viewModel { GirhubViewModel(get()) }


    // Profile
    single<IProfileRepository> { ProfileRepository() }
    factory { GetProfileUseCase(get()) }
    viewModel { ProfileViewModel(get()) }


    //Login
    viewModel{ LogInViewModel(get()) }
    factory { LoginUseCase(get()) }
    single<ILoginRepository> { LoginRepository() }


    //Movies
    //single { AppRoomDatabaseMovies.getDatabase(get()) }
    //single { get<AppRoomDatabaseMovies>().movieDao() }
    single<IMovieDao>{ get<AppRoomDatabase>().movieDao() }
    single<MovieService> {
        get<Retrofit>(named("movies")).create(MovieService::class.java)
    }
    single { MovieLocalDataSource(get()) }
    single { MovieRemoteDataSource(get(), get()) }
    single<IMovieRepository> { MovieRepository(get(), get()) }
    single<MovieRepository>{ MovieRepository(get(),get()) }
    factory { GetMoviesUseCase(get()) }
    viewModel { MoviesViewModel(get(), get()) }


    //RoomDatabase
    //single { AppRoomDatabaseA.getDatabase(get()) }
    //single { get<AppRoomDatabaseA>().dollarDao() }
    single { AppRoomDatabase.getDatabase(get()) }


    //Dollar
    single<IDollarDao> { get<AppRoomDatabase>().dollarDao() }
    single{
        RealTimeRemoteDataSource()
    }
    single { DollarLocalDataSource(get()) }
    single<IDollarRepository> { DollarRepository(get(), get()) }
    single<DollarRepository>{ DollarRepository(get(), get()) }
    factory { FetchDollarUseCase(get()) }
    viewModel{ DollarViewModel(get(), get()) }

    // Maintenance
    single { MaintenanceDataStore(get()) }
    single {
        FirebaseRemoteConfig.getInstance()
    }
    single { MaintenanceRepository(get(), get()) }
    viewModel { MaintenanceViewModel(get()) }
}