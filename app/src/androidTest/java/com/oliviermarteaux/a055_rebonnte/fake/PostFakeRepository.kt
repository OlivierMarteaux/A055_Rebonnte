package com.oliviermarteaux.a055_rebonnte.fake

import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Comment
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post
import com.oliviermarteaux.shared.firebase.firestore.utils.generateSamplePosts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PostFakeRepository (): PostRepository {
    /**
     * Retrieves a Flow object containing a list of Posts ordered by creation date
     * in descending order.
     *
     * @return Flow containing a list of Posts.
     */
    override val posts: Flow<Result<List<Post>>> =
        flowOf(Result.success(generateSamplePosts().sortedByDescending { it.timestamp }))

    /**
     * Adds a new Post to the data source using the injected PostApi.
     *
     * @param post The Post object to be added.
     */
    override suspend fun addPost(post: Post): Result<Unit> =
        Result.success(Unit)

    /**
     * Adds a new comment to the data source using the injected PostApi.
     *
     * @param comment The comment to be added.
     * @param postId The ID of the post associated with the comment.
     */
    override suspend fun addComment(postId: String, comment: Comment): Result<Unit> =
        Result.success(Unit)
}
