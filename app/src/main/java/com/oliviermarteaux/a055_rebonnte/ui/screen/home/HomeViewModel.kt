package com.oliviermarteaux.a055_rebonnte.ui.screen.home

import kotlin.collections.filter
import kotlin.collections.sortedWith
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post
import com.oliviermarteaux.shared.firebase.firestore.utils.uploadSamplePosts
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing data and events related to the Home screen.
 * This ViewModel retrieves posts from the PostRepository and exposes them as a Flow<List<Post>>,
 * allowing UI components to observe and react to changes in the posts data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val log: Logger,
    private val isOnlineFlow: Flow<Boolean>
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log,
) {
    /**
     * The UI state for the home feed.
     */
    var homeUiState: ListUiState<Post> by mutableStateOf(ListUiState.Loading)
        private set

    private var posts: List<Post> by mutableStateOf(emptyList())

    var filteredPosts: List<Post> by mutableStateOf(emptyList())
        private set

    var currentSortOption: SortOption? by mutableStateOf(null)
        private set

    var queryFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun clearQuery() {
        queryFieldValue = TextFieldValue("")
        filterPosts(queryFieldValue)
    }

    fun filterPosts(query: TextFieldValue) {
        queryFieldValue = query
        filteredPosts = posts.filter { post ->
            listOfNotNull(post.title, post.author?.firstname, post.author?.lastname)
                .any { field -> field.contains(query.text, true) }
        }.sortedWith ( currentSortOption?.comparator?:compareBy { null } )
    }

    fun sortPostsBy(sortOption: SortOption) {
        currentSortOption = sortOption
        filteredPosts = filteredPosts.sortedWith(sortOption.comparator)
    }

    /**
     * Loads the posts from the repository.
     */
    fun loadPosts() {
        viewModelScope.launch {
            homeUiState = ListUiState.Loading
//            delay(1500) // simulate network delay for Loading state evidence
            postRepository.posts.collect { result ->
                result
                    .onSuccess {
                        posts = it
                        filteredPosts = it
                        homeUiState =
                            if (posts.isEmpty()) ListUiState.Empty
                            else ListUiState.Success(posts)
                    }
                    .onFailure { e ->
                        homeUiState = ListUiState.Error(e)
                    }
            }
        }
    }

    /**
     * upload a list of sample posts to firestore for app demonstration purpose
     */
    fun uploadSamplePosts(context: Context){
        viewModelScope.launch {
            uploadSamplePosts(context){ post -> postRepository.addPost(post) } }
    }

    private fun signInTestUser(){
        viewModelScope.launch {
            userRepository.signIn(
                email = "fievel.farwest@example.com",
                password = "test123&",
            )
        }
    }

    init {
//        setAuthObserverDelay(1000)
//    throw RuntimeException("Test Crash") // Force a crash
        log.d("HomeFeedViewModel: init")

        // Sign in the test user in case of test config
//        if (BuildConfig.DEBUG) signInTestUser()
        if (TestConfig.isTest) signInTestUser()

        // Fetch posts from the repository
        loadPosts()
    }
}