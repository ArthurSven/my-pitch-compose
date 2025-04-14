package com.devapps.mypitch

import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.repository.FirebasePitchRepository
import com.devapps.mypitch.ui.utils.state.Response
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

class FirebaseRepositoryTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference
    private lateinit var document: DocumentReference
    private lateinit var query: Query
    private lateinit var querySnapshot: QuerySnapshot
    private lateinit var documentSnapshot: DocumentSnapshot
    private lateinit var repository: FirebasePitchRepository

    @Before
    fun setUp() {
        firestore = mock(FirebaseFirestore::class.java)
        collection = mock(CollectionReference::class.java)
        document = mock(DocumentReference::class.java)
        query = mock(Query::class.java)
        querySnapshot = mock(QuerySnapshot::class.java)
        documentSnapshot = mock(DocumentSnapshot::class.java)

        `when`(firestore.collection("pitches")).thenReturn(collection)
        `when`(collection.document(any())).thenReturn(document)
        `when`(collection.orderBy(anyString(), any())).thenReturn(query)
        `when`(collection.whereEqualTo(anyString(), anyString())).thenReturn(query)
        `when`(query.orderBy(anyString(), any())).thenReturn(query)

        repository = FirebasePitchRepository(firestore)
    }

    @Test
    fun `createPitch should return Success when pitch is created successfully`() = runBlocking {
        // Given
        val pitch = Pitch(
            pitchname = "Test Pitch",
            category = "Tech",
            description = "Test Description",
            google_id = "user123",
            username = "testuser",
            email = "test@example.com"
        )

        val task = mock(Task::class.java) as Task<Void>
        `when`(document.set(any())).thenReturn(task)
        `when`(task.await()).thenReturn(null)

        // When
        val result = repository.createPitch(pitch)

        // Then
        assertTrue(result is Response.Success)

        val captor = argumentCaptor<Map<String, Any>>()
        verify(document).set(captor.capture())
        val capturedData = captor.firstValue

        assertEquals(pitch.pitchname, capturedData["pitchname"])
        assertEquals(pitch.category, capturedData["category"])
        assertEquals(pitch.description, capturedData["description"])
        assertEquals(pitch.google_id, capturedData["google_id"])
        assertEquals(pitch.username, capturedData["username"])
        assertEquals(pitch.email, capturedData["email"])
        assertNotNull(capturedData["created_at"])
    }

    @Test
    fun `createPitch should return Error when exception occurs`() = runBlocking {
        // Given
        val pitch = Pitch(
            pitchname = "Test Pitch",
            category = "Tech",
            description = "Test Description",
            google_id = "user123",
            username = "testuser",
            email = "test@example.com"
        )

        val task = mock(Task::class.java) as Task<Void>
        `when`(document.set(any())).thenReturn(task)
        `when`(task.await()).thenThrow(RuntimeException("Firestore error"))

        // When
        val result = repository.createPitch(pitch)

        // Then
        assertTrue(result is Response.Error)
        assertEquals("Pitch creation failed: Firestore error", (result as Response.Error))
    }

}