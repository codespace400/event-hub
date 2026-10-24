package com.example.data

import com.example.model.Attendee
import com.example.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class CheckInResult {
    data class Success(val attendee: Attendee, val event: Event) : CheckInResult()
    data class AlreadyCheckedIn(val attendee: Attendee, val event: Event, val time: String) : CheckInResult()
    data class NotFound(val query: String) : CheckInResult()
    data class WrongEvent(val attendee: Attendee, val eventName: String) : CheckInResult()
}

class EventRepository {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _attendees = MutableStateFlow<List<Attendee>>(emptyList())
    val attendees: StateFlow<List<Attendee>> = _attendees.asStateFlow()

    init {
        initSampleData()
    }

    private fun initSampleData() {
        val event1 = Event(
            id = "evt_tech_summit",
            name = "Tech & AI Global Summit 2026",
            description = "Explore cutting-edge breakthroughs in generative AI, edge computing, robotics, and cloud architecture. Features 25+ keynote speakers, interactive labs, and premier networking sessions.",
            date = "Nov 15, 2026",
            time = "09:00 AM - 05:30 PM",
            venue = "Metro Tech Center, Grand Auditorium",
            coverImage = "cover_tech_summit",
            capacity = 250,
            registeredCount = 184,
            price = "$99",
            category = "Technology",
            isPublished = true,
            organizerName = "EventHub Productions"
        )

        val event2 = Event(
            id = "evt_music_fest",
            name = "Neon Pulse Music Festival",
            description = "An immersive audio-visual music spectacle featuring world-class electronic producers, indie live acts, light installations, and gourmet food trucks under the stars.",
            date = "Dec 04, 2026",
            time = "04:00 PM - 11:30 PM",
            venue = "Sunset Arena & Open Park Grounds",
            coverImage = "cover_music_fest",
            capacity = 500,
            registeredCount = 412,
            price = "$65",
            category = "Music",
            isPublished = true,
            organizerName = "Electric Horizons LLC"
        )

        val event3 = Event(
            id = "evt_design_expo",
            name = "Modern UX & Product Design Expo",
            description = "A full-day exhibition and creative workshop for product designers, creative directors, and front-end innovators showcasing future design systems, 3D spatial design, and micro-interactions.",
            date = "Oct 30, 2026",
            time = "10:00 AM - 04:00 PM",
            venue = "Creative Arts Pavilion, Space 3",
            coverImage = "cover_design_expo",
            capacity = 150,
            registeredCount = 98,
            price = "Free",
            category = "Design",
            isPublished = true,
            organizerName = "DesignCraft Collective"
        )

        val event4 = Event(
            id = "evt_startup_pitch",
            name = "NextGen Startup Pitch & Mixer",
            description = "Join top venture capitalists, angel investors, and 12 curated early-stage tech startups competing for $250k non-dilutive seed grants followed by VIP drinks.",
            date = "Nov 28, 2026",
            time = "06:30 PM - 09:30 PM",
            venue = "Founders Hub, 4th Floor Sky Lounge",
            coverImage = "cover_tech_summit",
            capacity = 80,
            registeredCount = 76,
            price = "Free",
            category = "Networking",
            isPublished = true,
            organizerName = "SeedStage Ventures"
        )

        _events.value = listOf(event1, event2, event3, event4)

        // Seed sample attendees
        val initialAttendees = mutableListOf(
            // Attendees for Tech Summit
            Attendee(
                id = "att_1",
                eventId = "evt_tech_summit",
                ticketCode = "EH-8910-T",
                name = "Jordan Attendee",
                email = "jordan.attendee@example.com",
                phone = "+1 (555) 234-5678",
                ticketType = "VIP All-Access",
                quantity = 1,
                isCheckedIn = true,
                checkedInAt = System.currentTimeMillis() - 3600000
            ),
            Attendee(
                id = "att_2",
                eventId = "evt_tech_summit",
                ticketCode = "EH-7421-T",
                name = "Sophia Chen",
                email = "sophia.chen@innovate.io",
                phone = "+1 (555) 987-6543",
                ticketType = "General Admission",
                quantity = 1,
                isCheckedIn = false,
                checkedInAt = null
            ),
            Attendee(
                id = "att_3",
                eventId = "evt_tech_summit",
                ticketCode = "EH-6192-T",
                name = "Marcus Vance",
                email = "marcus.v@cloudtech.com",
                phone = "+1 (555) 345-6789",
                ticketType = "General Admission",
                quantity = 2,
                isCheckedIn = true,
                checkedInAt = System.currentTimeMillis() - 7200000
            ),
            Attendee(
                id = "att_4",
                eventId = "evt_tech_summit",
                ticketCode = "EH-5531-T",
                name = "Elena Rostova",
                email = "elena.r@ai-labs.org",
                phone = "+1 (555) 456-7890",
                ticketType = "Speaker Pass",
                quantity = 1,
                isCheckedIn = false,
                checkedInAt = null
            ),
            Attendee(
                id = "att_5",
                eventId = "evt_tech_summit",
                ticketCode = "EH-4288-T",
                name = "David Kim",
                email = "david.kim@robotics.dev",
                phone = "+1 (555) 567-8901",
                ticketType = "General Admission",
                quantity = 1,
                isCheckedIn = false,
                checkedInAt = null
            ),

            // Attendees for Music Fest
            Attendee(
                id = "att_6",
                eventId = "evt_music_fest",
                ticketCode = "EH-9120-M",
                name = "Jordan Attendee",
                email = "jordan.attendee@example.com",
                phone = "+1 (555) 234-5678",
                ticketType = "Tier 1 Early Bird",
                quantity = 2,
                isCheckedIn = false,
                checkedInAt = null
            ),
            Attendee(
                id = "att_7",
                eventId = "evt_music_fest",
                ticketCode = "EH-3319-M",
                name = "Liam Miller",
                email = "liam.m@musicfan.net",
                phone = "+1 (555) 678-9012",
                ticketType = "VIP Backstage",
                quantity = 1,
                isCheckedIn = true,
                checkedInAt = System.currentTimeMillis() - 1800000
            ),
            Attendee(
                id = "att_8",
                eventId = "evt_music_fest",
                ticketCode = "EH-8842-M",
                name = "Zoe Martinez",
                email = "zoe.martinez@beats.fm",
                phone = "+1 (555) 789-0123",
                ticketType = "General Admission",
                quantity = 1,
                isCheckedIn = false,
                checkedInAt = null
            ),

            // Attendees for Design Expo
            Attendee(
                id = "att_9",
                eventId = "evt_design_expo",
                ticketCode = "EH-1204-D",
                name = "Jordan Attendee",
                email = "jordan.attendee@example.com",
                phone = "+1 (555) 234-5678",
                ticketType = "Free Pass",
                quantity = 1,
                isCheckedIn = false,
                checkedInAt = null
            ),
            Attendee(
                id = "att_10",
                eventId = "evt_design_expo",
                ticketCode = "EH-6651-D",
                name = "Chloe Dubois",
                email = "chloe.d@uxstudio.co",
                phone = "+1 (555) 890-1234",
                ticketType = "Workshop Pass",
                quantity = 1,
                isCheckedIn = true,
                checkedInAt = System.currentTimeMillis() - 5400000
            )
        )

        _attendees.value = initialAttendees
    }

    fun getEventById(id: String): Event? {
        return _events.value.find { it.id == id }
    }

    fun getAttendeesForEvent(eventId: String): List<Attendee> {
        return _attendees.value.filter { it.eventId == eventId }
    }

    fun getMyTickets(email: String): List<Pair<Attendee, Event>> {
        val myRegistrations = _attendees.value.filter { it.email.equals(email.trim(), ignoreCase = true) }
        return myRegistrations.mapNotNull { attendee ->
            val event = getEventById(attendee.eventId)
            if (event != null) Pair(attendee, event) else null
        }
    }

    fun addEvent(event: Event) {
        _events.value = listOf(event) + _events.value
    }

    fun updateEvent(event: Event) {
        _events.value = _events.value.map { if (it.id == event.id) event else it }
    }

    fun deleteEvent(eventId: String) {
        _events.value = _events.value.filter { it.id != eventId }
        _attendees.value = _attendees.value.filter { it.eventId != eventId }
    }

    fun registerAttendee(
        eventId: String,
        name: String,
        email: String,
        phone: String,
        quantity: Int = 1,
        ticketType: String = "General Admission"
    ): Attendee {
        val codeSuffix = ('A'..'Z').random()
        val randomNum = (1000..9999).random()
        val code = "EH-$randomNum-$codeSuffix"

        val newAttendee = Attendee(
            id = "att_${UUID.randomUUID().toString().take(8)}",
            eventId = eventId,
            ticketCode = code,
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            ticketType = ticketType,
            quantity = quantity,
            registeredAt = System.currentTimeMillis(),
            isCheckedIn = false,
            checkedInAt = null
        )

        _attendees.value = listOf(newAttendee) + _attendees.value

        // Increment event registered count
        _events.value = _events.value.map { evt ->
            if (evt.id == eventId) {
                evt.copy(registeredCount = evt.registeredCount + quantity)
            } else evt
        }

        return newAttendee
    }

    fun toggleCheckIn(attendeeId: String) {
        _attendees.value = _attendees.value.map { att ->
            if (att.id == attendeeId) {
                val newStatus = !att.isCheckedIn
                att.copy(
                    isCheckedIn = newStatus,
                    checkedInAt = if (newStatus) System.currentTimeMillis() else null
                )
            } else att
        }
    }

    fun checkIn(targetEventId: String, query: String): CheckInResult {
        val cleanedQuery = query.trim()
        val targetEvent = getEventById(targetEventId) ?: return CheckInResult.NotFound(cleanedQuery)

        // Find by exact ticket code (case-insensitive) or by attendee name match
        val matchedAttendee = _attendees.value.find { att ->
            att.ticketCode.equals(cleanedQuery, ignoreCase = true) ||
            att.name.equals(cleanedQuery, ignoreCase = true) ||
            att.email.equals(cleanedQuery, ignoreCase = true)
        }

        if (matchedAttendee == null) {
            return CheckInResult.NotFound(cleanedQuery)
        }

        // Check if ticket is for this event
        if (matchedAttendee.eventId != targetEventId) {
            val associatedEvent = getEventById(matchedAttendee.eventId)
            return CheckInResult.WrongEvent(
                matchedAttendee,
                associatedEvent?.name ?: "Different Event"
            )
        }

        if (matchedAttendee.isCheckedIn) {
            val timeStr = matchedAttendee.checkedInAt?.let {
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(it))
            } ?: "earlier today"
            return CheckInResult.AlreadyCheckedIn(matchedAttendee, targetEvent, timeStr)
        }

        // Perform check in
        val now = System.currentTimeMillis()
        val updatedAttendee = matchedAttendee.copy(
            isCheckedIn = true,
            checkedInAt = now
        )

        _attendees.value = _attendees.value.map {
            if (it.id == matchedAttendee.id) updatedAttendee else it
        }

        return CheckInResult.Success(updatedAttendee, targetEvent)
    }
}
