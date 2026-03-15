package com.popop.lifesimulator.data.models.relationship

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.popop.lifesimulator.data.models.character.Gender
import com.popop.lifesimulator.data.models.character.PrimaryStats
import com.popop.lifesimulator.data.models.character.SecondaryStats
import com.popop.lifesimulator.data.models.character.TraitLoadout
import com.popop.lifesimulator.data.models.character.SkillRegistry
import com.popop.lifesimulator.data.models.world.FactionCategory
import com.popop.lifesimulator.data.models.world.LocationType

/**
 * NPC - Non-Player Character
 */
@Entity(tableName = "npcs")
data class Npc(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    
    // Identity
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: Gender,
    
    // Appearance
    val appearance: String = "",  // JSON of appearance details
    val height: Int = 170,  // cm
    val build: BodyBuild = BodyBuild.AVERAGE,
    
    // Stats (simplified for NPCs)
    val health: Int = 100,
    val charisma: Int = 50,
    val intellect: Int = 50,
    val cunning: Int = 50,
    val violence: Int = 50,
    val reputation: Int = 0,
    val wealth: Double = 0.0,
    
    // Personality
    val personalityType: PersonalityType = PersonalityType.TYPE_A,
    val moralAlignment: MoralAlignment = MoralAlignment.TRUE_NEUTRAL,
    val temperament: Temperament = Temperament.SANGUINE,
    
    // Traits and skills
    val traitsJson: String = "[]",  // JSON of trait IDs
    val skillsJson: String = "{}",  // JSON of skills
    
    // Background
    val occupation: String = "",
    val backstory: String = "",
    val secrets: String = "",  // JSON of secrets
    
    // Affiliations
    val factionId: Long? = null,
    val factionCategory: FactionCategory? = null,
    val locationId: Long? = null,
    val homeLocationId: Long? = null,
    
    // Relationships
    val familyIds: String = "",  // JSON of NPC IDs
    val friendIds: String = "",  // JSON of NPC IDs
    val enemyIds: String = "",  // JSON of NPC IDs
    val relationshipWithPlayer: String? = null,  // JSON of relationship data
    
    // AI behavior
    val dailyRoutine: String = "",  // JSON of routine
    val currentGoal: String? = null,
    val longTermAmbition: String? = null,
    val fearList: String = "",  // JSON of fears
    val desireList: String = "",  // JSON of desires
    
    // State
    val isAlive: Boolean = true,
    val isImportant: Boolean = false,  // Plot-critical NPC
    val isMerchant: Boolean = false,
    val isRomanceable: Boolean = false,
    
    // Memory
    val memoryOfPlayer: String = "",  // JSON of player interactions
    val opinionOfPlayer: Int = 0,  // -100 to +100
    
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getFullName(): String = "$firstName $lastName"
    
    fun getAgeCategory(): AgeCategory = when {
        age < 13 -> AgeCategory.CHILD
        age < 20 -> AgeCategory.TEENAGER
        age < 30 -> AgeCategory.YOUNG_ADULT
        age < 50 -> AgeCategory.ADULT
        age < 65 -> AgeCategory.MIDDLE_AGED
        else -> AgeCategory.SENIOR
    }
    
    fun getPrimaryStats(): PrimaryStats = PrimaryStats(
        health = health,
        charisma = charisma,
        intellect = intellect,
        cunning = cunning,
        violence = violence
    )
    
    fun getSecondaryStats(): SecondaryStats = SecondaryStats(
        reputation = reputation,
        wealth = wealth
    )
    
    fun getTraits(): TraitLoadout = TraitLoadout(
        traits = try {
            org.json.JSONArray(traitsJson).let { arr ->
                List(arr.length()) { arr.getString(it) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    )
}

enum class BodyBuild(val displayName: String) {
    THIN("Thin"),
    AVERAGE("Average"),
    ATHLETIC("Athletic"),
    MUSCULAR("Muscular"),
    HEAVY("Heavy"),
    OBESE("Obese")
}

enum class PersonalityType(val displayName: String) {
    TYPE_A("Type A (Ambitious, Driven)"),
    TYPE_B("Type B (Relaxed, Easy-going)"),
    TYPE_C("Type C (Detail-oriented, Conscientious)"),
    TYPE_D("Type D (Distressed, Anxious)")
}

enum class MoralAlignment(val displayName: String) {
    LAWFUL_GOOD("Lawful Good"),
    NEUTRAL_GOOD("Neutral Good"),
    CHAOTIC_GOOD("Chaotic Good"),
    LAWFUL_NEUTRAL("Lawful Neutral"),
    TRUE_NEUTRAL("True Neutral"),
    CHAOTIC_NEUTRAL("Chaotic Neutral"),
    LAWFUL_EVIL("Lawful Evil"),
    NEUTRAL_EVIL("Neutral Evil"),
    CHAOTIC_EVIL("Chaotic Evil")
}

enum class Temperament(val displayName: String) {
    SANGUINE("Sanguine (Optimistic, Social)"),
    CHOLERIC("Choleric (Ambitious, Leader-like)"),
    MELANCHOLIC("Melancholic (Analytical, Quiet)"),
    PHLEGMATIC("Phlegmatic (Relaxed, Peaceful)")
}

enum class AgeCategory(val displayName: String) {
    CHILD("Child"),
    TEENAGER("Teenager"),
    YOUNG_ADULT("Young Adult"),
    ADULT("Adult"),
    MIDDLE_AGED("Middle-Aged"),
    SENIOR("Senior")
}

/**
 * NPC Generator for creating random NPCs
 */
object NpcGenerator {
    
    private val MALE_NAMES = listOf(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph",
        "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald",
        "Mark", "Paul", "Steven", "Andrew", "Kenneth", "Joshua", "Kevin", "Brian",
        "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob"
    )
    
    private val FEMALE_NAMES = listOf(
        "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica",
        "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret", "Sandra", "Ashley",
        "Kimberly", "Emily", "Donna", "Michelle", "Dorothy", "Carol", "Amanda", "Melissa",
        "Deborah", "Stephanie", "Rebecca", "Sharon", "Laura", "Cynthia", "Kathleen"
    )
    
    private val LAST_NAMES = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson"
    )
    
    private val OCCUPATIONS = listOf(
        "Teacher", "Doctor", "Nurse", "Engineer", "Lawyer", "Police Officer", "Firefighter",
        "Chef", "Artist", "Writer", "Musician", "Actor", "Scientist", "Programmer",
        "Accountant", "Salesperson", "Manager", "Contractor", "Electrician", "Plumber",
        "Mechanic", "Farmer", "Driver", "Pilot", "Soldier", "Detective", "Judge",
        "Politician", "Business Owner", "Scientist", "Professor", "Student", "Unemployed"
    )
    
    fun generateRandomNpc(
        minAge: Int = 18,
        maxAge: Int = 65,
        gender: Gender? = null,
        occupation: String? = null
    ): Npc {
        val selectedGender = gender ?: if (Math.random() < 0.5) Gender.MALE else Gender.FEMALE
        val firstName = when (selectedGender) {
            Gender.MALE -> MALE_NAMES.random()
            Gender.FEMALE -> FEMALE_NAMES.random()
            Gender.NON_BINARY -> (MALE_NAMES + FEMALE_NAMES).random()
        }
        val lastName = LAST_NAMES.random()
        val age = (minAge..maxAge).random()
        val selectedOccupation = occupation ?: OCCUPATIONS.random()
        
        return Npc(
            firstName = firstName,
            lastName = lastName,
            age = age,
            gender = selectedGender,
            occupation = selectedOccupation,
            health = (70..100).random(),
            charisma = (20..80).random(),
            intellect = (20..80).random(),
            cunning = (20..80).random(),
            violence = (10..70).random(),
            reputation = (-20..20).random(),
            wealth = (0.0..10000.0).random(),
            personalityType = PersonalityType.entries.random(),
            moralAlignment = MoralAlignment.entries.random(),
            temperament = Temperament.entries.random(),
            build = BodyBuild.entries.random()
        )
    }
    
    fun generateFamilyMember(
        relation: RelationshipType,
        playerAge: Int,
        gender: Gender? = null
    ): Npc {
        val npcAge = when (relation) {
            RelationshipType.PARENT_CHILD -> playerAge + (20..40).random()
            RelationshipType.CHILD_PARENT -> (0..playerAge.coerceAtMost(17)).random()
            RelationshipType.SIBLING -> (playerAge - 10..playerAge + 10).coerceAtLeast(0).random()
            RelationshipType.GRANDPARENT_GRANDCHILD -> playerAge + (50..80).random()
            else -> playerAge
        }
        
        return generateRandomNpc(
            minAge = npcAge.coerceAtLeast(0),
            maxAge = npcAge + 1,
            gender = gender
        )
    }
}
