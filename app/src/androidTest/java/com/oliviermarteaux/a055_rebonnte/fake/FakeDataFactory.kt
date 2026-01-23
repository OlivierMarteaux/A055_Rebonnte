package com.oliviermarteaux.a055_rebonnte.fake

import com.oliviermarteaux.shared.extensions.toDate
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Address
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Comment
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post
import java.time.LocalDate
import java.time.LocalTime

class FakeDataFactory {

    val user1 = User("u1", "Alice", "Smith", "alice.smith@example.com")
    val user2 = User("u2", "Bob", "Johnson", "bob.johnson@example.com")

    val fakePostList = listOf(
        Post(
            title = "Christmas Village Opening",
            description = "Come enjoy the festive Christmas village with lights, food, and gifts!",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 20,
            author = user1,
            comments = listOf(
                Comment(user2, "Can't wait!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 11, 5).toDate(), // Dec 5, 2025
            time = LocalTime.of(10, 0).toDate(),
            address = Address("Main Street", "Downtown", "Paris", "75001", "France")
        ),
        Post(
            title = "City Marathon 2025",
            description = "Annual city marathon open to all runners.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 16,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 4, 12).toDate(), // May 12, 2025
            time = LocalTime.of(11, 0).toDate(),
            address = Address("Park Avenue", "East Side", "New York", "10010", "USA")
        ),
        Post(
            title = "Grand Mall Opening",
            description = "Join us for the grand opening of Central Mall with special discounts.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 19,
            author = user1,
            comments = listOf(
                Comment(user2, "Excited to shop!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 6, 20).toDate(),
            time = LocalTime.of(12, 0).toDate(),
            address = Address("Market Street", "Uptown", "London", "E1 6AN", "UK")
        ),
        Post(
            title = "Food Festival",
            description = "Taste the best street food in the city.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 18,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 7, 15).toDate(),
            time = LocalTime.of(13, 0).toDate(),
            address = Address("Riverside Blvd", "Old Town", "Berlin", "10115", "Germany")
        ),
        Post(
            title = "Summer Concert Series",
            description = "Enjoy live music at the city park every weekend.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 17,
            author = user1,
            comments = emptyList(),
            date = LocalDate.of(2025, 6, 1).toDate(),
            time = LocalTime.of(14, 0).toDate(),
            address = Address("Central Park", "Midtown", "Toronto", "M5V", "Canada")
        ),
        Post(
            title = "Winter Ice Skating Rink",
            description = "Open-air ice skating rink in downtown area.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 14,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 11, 20).toDate(),
            time = LocalTime.of(15, 0).toDate(),
            address = Address("Lakeview Street", "Center", "Stockholm", "111 21", "Sweden")
        ),
        Post(
            title = "Local Art Exhibition",
            description = "Showcasing local artists' work from around the city.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 6,
            author = user1,
            comments = listOf(
                Comment(user2, "Amazing pieces!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 3, 18).toDate(),
            time = LocalTime.of(16, 0).toDate(),
            address = Address("Art Street", "Cultural District", "Rome", "00186", "Italy")
        ),
        Post(
            title = "City Parade",
            description = "Annual parade celebrating city culture.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 13,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 8, 10).toDate(),
            time = LocalTime.of(17, 0).toDate(),
            address = Address("Main Boulevard", "City Center", "Madrid", "28013", "Spain")
        ),
        Post(
            title = "Jazz Night",
            description = "Evening of live jazz music at the riverfront.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 10,
            author = user1,
            comments = listOf(
                Comment(user2, "Love jazz!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 5, 25).toDate(),
            time = LocalTime.of(18, 0).toDate(),
            address = Address("River Street", "Old Quarter", "Lisbon", "1100-148", "Portugal")
        ),
        Post(
            title = "Technology Expo",
            description = "Exhibition of the latest gadgets and innovations.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 15,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 9, 2).toDate(),
            time = LocalTime.of(19, 0).toDate(),
            address = Address("Tech Park", "Innovation District", "San Francisco", "94103", "USA")
        ),
        Post(
            title = "Farmers Market",
            description = "Fresh local produce every Saturday morning.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 12,
            author = user1,
            comments = emptyList(),
            date = LocalDate.of(2025, 2, 12).toDate(),
            time = LocalTime.of(20, 0).toDate(),
            address = Address("Market Lane", "Downtown", "Amsterdam", "1012", "Netherlands")
        ),
        Post(
            title = "Halloween Festival",
            description = "Spooky activities for the whole family.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 9,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 9, 30).toDate(),
            time = LocalTime.of(21, 0).toDate(),
            address = Address("Pumpkin Street", "Old Town", "Dublin", "D01", "Ireland")
        ),
        Post(
            title = "Book Fair",
            description = "Discover new authors and rare books.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 8,
            author = user1,
            comments = listOf(
                Comment(user2, "Can't wait!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 6, 10).toDate(),
            time = LocalTime.of(22, 0).toDate(),
            address = Address("Library Road", "Central", "Edinburgh", "EH1", "UK")
        ),
        Post(
            title = "Science Festival",
            description = "Interactive science exhibitions for all ages.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 7,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 4, 5).toDate(),
            time = LocalTime.of(23, 0).toDate(),
            address = Address("Innovation Street", "Tech District", "Copenhagen", "1050", "Denmark")
        ),
        Post(
            title = "Summer Food Truck Rally",
            description = "Try delicious street food from around the city.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 11,
            author = user1,
            comments = emptyList(),
            date = LocalDate.of(2025, 7, 8).toDate(),
            time = LocalTime.of(0, 0).toDate(),
            address = Address("Riverfront", "Downtown", "Melbourne", "3000", "Australia")
        ),
        Post(
            title = "City Film Festival",
            description = "Screenings of independent films across multiple theaters.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 5,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 5, 18).toDate(),
            time = LocalTime.of(1, 0).toDate(),
            address = Address(
                "Cinema Street",
                "Entertainment District",
                "Los Angeles",
                "90028",
                "USA"
            )
        ),
        Post(
            title = "Yoga in the Park",
            description = "Morning yoga sessions for everyone.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 4,
            author = user1,
            comments = emptyList(),
            date = LocalDate.of(2025, 6, 22).toDate(),
            time = LocalTime.of(2, 0).toDate(),
            address = Address("Green Park", "Midtown", "London", "SW1A", "UK")
        ),
        Post(
            title = "City Carnival",
            description = "Annual carnival with rides, games, and food.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 1,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 8, 14).toDate(),
            time = LocalTime.of(3, 0).toDate(),
            address = Address("Fun Street", "Downtown", "Rio de Janeiro", "20010", "Brazil")
        ),
        Post(
            title = "Open Air Theater",
            description = "Enjoy plays under the stars.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 3,
            author = user1,
            comments = listOf(
                Comment(user2, "Lovely idea!", System.currentTimeMillis())
            ),
            date = LocalDate.of(2025, 7, 30).toDate(),
            time = LocalTime.of(4, 0).toDate(),
            address = Address("Theater Avenue", "Cultural District", "Paris", "75002", "France")
        ),
        Post(
            title = "Winter Light Festival",
            description = "City streets illuminated with spectacular light installations.",
            photoUrl = "https://picsum.photos/800/600?random=1",
            timestamp = 2,
            author = user2,
            comments = emptyList(),
            date = LocalDate.of(2025, 11, 10).toDate(),
            time = LocalTime.of(5, 0).toDate(),
            address = Address("Lantern Street", "Old Town", "Tokyo", "100-0001", "Japan")
        )
    )
}