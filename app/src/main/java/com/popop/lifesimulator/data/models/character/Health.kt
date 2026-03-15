package com.popop.lifesimulator.data.models.character

/**
 * Injury severity levels
 */
enum class InjurySeverity(val displayName: String, val healthImpact: Int, val healingTimeDays: Int) {
    MINOR("Minor", -5, 3),
    MODERATE("Moderate", -15, 7),
    SEVERE("Severe", -30, 14),
    CRITICAL("Critical", -50, 30),
    LIFE_THREATENING("Life-Threatening", -80, 60)
}

/**
 * Body part locations for injuries
 */
enum class BodyPart(val displayName: String) {
    HEAD("Head"),
    CHEST("Chest"),
    STOMACH("Stomach"),
    LEFT_ARM("Left Arm"),
    RIGHT_ARM("Right Arm"),
    LEFT_LEG("Left Leg"),
    RIGHT_LEG("Right Leg"),
    HAND("Hand"),
    FOOT("Foot"),
    FULL_BODY("Full Body")
}

/**
 * Injury types
 */
enum class InjuryType(val displayName: String) {
    CUT("Cut/Laceration"),
    BRUISE("Bruise/Contusion"),
    FRACTURE("Fracture"),
    DISLOCATION("Dislocation"),
    BURN("Burn"),
    STAB_WOUND("Stab Wound"),
    GUNSHOT_WOUND("Gunshot Wound"),
    CONCUSSION("Concussion"),
    SPRAIN("Sprain/Strain"),
    INTERNAL_BLEEDING("Internal Bleeding"),
    NERVE_DAMAGE("Nerve Damage"),
    ORGAN_DAMAGE("Organ Damage")
}

/**
 * Injury entity
 */
data class Injury(
    val id: String,
    val type: InjuryType,
    val severity: InjurySeverity,
    val bodyPart: BodyPart,
    val description: String,
    val cause: String,
    val inflictedAt: Long = System.currentTimeMillis(),
    val healingProgress: Int = 0,  // 0-100
    val isInfected: Boolean = false,
    val isBleeding: Boolean = false,
    val permanentDamage: Boolean = false,
    val statModifiers: Map<String, Int> = emptyMap()
) {
    fun getHealthImpact(): Int = severity.healthImpact
    
    fun getHealingTimeRemaining(): Int {
        val daysPassed = ((System.currentTimeMillis() - inflictedAt) / (1000 * 60 * 60 * 24)).toInt()
        return (severity.healingTimeDays - daysPassed).coerceAtLeast(0)
    }
    
    fun isHealed(): Boolean = healingProgress >= 100
    
    fun advanceHealing(amount: Int): Injury {
        return copy(healingProgress = (healingProgress + amount).coerceAtMost(100))
    }
}

/**
 * Illness types
 */
enum class IllnessType(val displayName: String, val severity: IllnessSeverity, val contagious: Boolean) {
    // Minor Illnesses
    COMMON_COLD("Common Cold", IllnessSeverity.MINOR, true),
    FLU("Influenza", IllnessSeverity.MODERATE, true),
    HEADACHE("Headache", IllnessSeverity.MINOR, false),
    STOMACH_BUG("Stomach Bug", IllnessSeverity.MINOR, true),
    ALLERGIES("Allergies", IllnessSeverity.MINOR, false),
    
    // Moderate Illnesses
    PNEUMONIA("Pneumonia", IllnessSeverity.MODERATE, true),
    BRONCHITIS("Bronchitis", IllnessSeverity.MODERATE, true),
    STREP_THROAT("Strep Throat", IllnessSeverity.MODERATE, true),
    MIGRAINE("Migraine", IllnessSeverity.MODERATE, false),
    FOOD_POISONING("Food Poisoning", IllnessSeverity.MODERATE, false),
    
    // Severe Illnesses
    TUBERCULOSIS("Tuberculosis", IllnessSeverity.SEVERE, true),
    HEPATITIS("Hepatitis", IllnessSeverity.SEVERE, true),
    MALARIA("Malaria", IllnessSeverity.SEVERE, true),
    APPENDICITIS("Appendicitis", IllnessSeverity.SEVERE, false),
    KIDNEY_STONES("Kidney Stones", IllnessSeverity.SEVERE, false),
    
    // Critical Illnesses
    CANCER("Cancer", IllnessSeverity.CRITICAL, false),
    HEART_DISEASE("Heart Disease", IllnessSeverity.CRITICAL, false),
    DIABETES("Diabetes", IllnessSeverity.CRITICAL, false),
    HIV_AIDS("HIV/AIDS", IllnessSeverity.CRITICAL, true),
    MENINGITIS("Meningitis", IllnessSeverity.CRITICAL, true),
    
    // Chronic Conditions
    ARTHRITIS("Arthritis", IllnessSeverity.CHRONIC, false),
    ASTHMA("Asthma", IllnessSeverity.CHRONIC, false),
    HYPERTENSION("Hypertension", IllnessSeverity.CHRONIC, false),
    DEPRESSION("Depression", IllnessSeverity.CHRONIC, false),
    ANXIETY_DISORDER("Anxiety Disorder", IllnessSeverity.CHRONIC, false)
}

enum class IllnessSeverity(val displayName: String, val healthDrain: Int) {
    MINOR("Minor", -1),
    MODERATE("Moderate", -3),
    SEVERE("Severe", -5),
    CRITICAL("Critical", -10),
    CHRONIC("Chronic", -2)
}

/**
 * Illness entity
 */
data class Illness(
    val id: String,
    val type: IllnessType,
    val severity: IllnessSeverity,
    val description: String,
    val contractedAt: Long = System.currentTimeMillis(),
    val symptoms: List<String> = emptyList(),
    val treatmentRequired: Boolean = false,
    val treatmentProgress: Int = 0,  // 0-100
    val isDiagnosed: Boolean = false,
    val medication: String? = null,
    val daysUntilRecovery: Int = 0
) {
    fun getDailyHealthDrain(): Int = severity.healthDrain
    
    fun isRecovered(): Boolean = treatmentProgress >= 100
    
    fun advanceTreatment(amount: Int): Illness {
        return copy(treatmentProgress = (treatmentProgress + amount).coerceAtMost(100))
    }
    
    fun getSymptomsText(): String = symptoms.joinToString(", ")
}

/**
 * Mental health conditions
 */
enum class MentalHealthCondition(val displayName: String, val severity: MentalHealthSeverity) {
    // Mood Disorders
    DEPRESSION_MILD("Mild Depression", MentalHealthSeverity.MILD),
    DEPRESSION_MODERATE("Moderate Depression", MentalHealthSeverity.MODERATE),
    DEPRESSION_SEVERE("Severe Depression", MentalHealthSeverity.SEVERE),
    BIPOLAR_DISORDER("Bipolar Disorder", MentalHealthSeverity.SEVERE),
    
    // Anxiety Disorders
    GENERALIZED_ANXIETY("Generalized Anxiety", MentalHealthSeverity.MODERATE),
    SOCIAL_ANXIETY("Social Anxiety", MentalHealthSeverity.MODERATE),
    PANIC_DISORDER("Panic Disorder", MentalHealthSeverity.SEVERE),
    PTSD("PTSD", MentalHealthSeverity.SEVERE),
    OCD("OCD", MentalHealthSeverity.MODERATE),
    
    // Personality Disorders
    BORDERLINE_PD("Borderline Personality Disorder", MentalHealthSeverity.SEVERE),
    NARCISSISTIC_PD("Narcissistic Personality Disorder", MentalHealthSeverity.MODERATE),
    ANTISOCIAL_PD("Antisocial Personality Disorder", MentalHealthSeverity.SEVERE),
    AVOIDANT_PD("Avoidant Personality Disorder", MentalHealthSeverity.MODERATE),
    
    // Other Conditions
    INSOMNIA("Insomnia", MentalHealthSeverity.MILD),
    EATING_DISORDER("Eating Disorder", MentalHealthSeverity.SEVERE),
    ADDICTION("Addiction", MentalHealthSeverity.SEVERE),
    SCHIZOPHRENIA("Schizophrenia", MentalHealthSeverity.SEVERE),
    DISSOCIATIVE_DISORDER("Dissociative Disorder", MentalHealthSeverity.SEVERE)
}

enum class MentalHealthSeverity(val displayName: String, val stressImpact: Int, val statPenalty: Int) {
    MILD("Mild", 5, -5),
    MODERATE("Moderate", 10, -10),
    SEVERE("Severe", 20, -20)
}

/**
 * Mental health state entity
 */
data class MentalHealthState(
    val conditions: List<MentalHealthConditionState> = emptyList(),
    val currentMood: Mood = Mood.NEUTRAL,
    val emotionalStability: Int = 50,  // 0-100
    val traumaLevel: Int = 0,  // 0-100
    val copingMechanisms: List<String> = emptyList(),
    val therapyProgress: Int = 0,  // 0-100
    val lastTherapySession: Long? = null,
    val medicationCompliance: Float = 1.0f  // 0.0 to 1.0
) {
    fun getOverallMentalHealth(): Int {
        val conditionPenalty = conditions.sumOf { it.severity.statPenalty }
        return (emotionalStability + conditionPenalty).coerceIn(0, 100)
    }
    
    fun isStable(): Boolean = emotionalStability > 60 && conditions.isEmpty()
    
    fun needsTreatment(): Boolean = conditions.any { it.severity != MentalHealthSeverity.MILD }
}

data class MentalHealthConditionState(
    val condition: MentalHealthCondition,
    val severity: MentalHealthSeverity,
    val diagnosedAt: Long = System.currentTimeMillis(),
    val triggers: List<String> = emptyList(),
    val treatmentPlan: String? = null
)

/**
 * Current mood states
 */
enum class Mood(val displayName: String, val color: Int) {
    ECSTATIC("Ecstatic", 0xFF48BB78),
    HAPPY("Happy", 0xFF68D391),
    CONTENT("Content", 0xFF9AE6B4),
    NEUTRAL("Neutral", 0xFFA0AEC0),
    SAD("Sad", 0xFF63B3ED),
    ANGRY("Angry", 0xFFF56565),
    ANXIOUS("Anxious", 0xFFF6AD55),
    DEPRESSED("Depressed", 0xFF6B46C1),
    STRESSED("Stressed", 0xFFFC8181),
    EXHAUSTED("Exhausted", 0xFF718096)
}

/**
 * Physical fitness state
 */
data class FitnessState(
    val bodyFatPercentage: Float = 20f,
    val muscleMass: Int = 50,  // 0-100
    val cardiovascularHealth: Int = 50,  // 0-100
    val flexibility: Int = 50,  // 0-100
    val endurance: Int = 50,  // 0-100
    val strength: Int = 50,  // 0-100
    val bmi: Float = 22f,
    val fitnessLevel: FitnessLevel = FitnessLevel.AVERAGE
) {
    fun getOverallFitness(): Int = (muscleMass + cardiovascularHealth + flexibility + endurance + strength) / 5
    
    fun updateFitnessLevel() {
        val overall = getOverallFitness()
        // Fitness level would be updated based on overall score
    }
}

enum class FitnessLevel(val displayName: String, val minScore: Int) {
    POOR("Poor", 0),
    BELOW_AVERAGE("Below Average", 30),
    AVERAGE("Average", 50),
    ABOVE_AVERAGE("Above Average", 70),
    EXCELLENT("Excellent", 85),
    ATHLETE("Athlete", 95)
}

/**
 * Comprehensive Health State
 */
data class HealthState(
    // Core stats
    val currentHealth: Int = 100,
    val maxHealth: Int = 100,
    val painLevel: Int = 0,  // 0-100
    val immunityLevel: Int = 50,  // 0-100
    val bodyTemperature: Float = 37f,  // Celsius
    val hydrationLevel: Int = 100,  // 0-100
    val hungerLevel: Int = 100,  // 0-100
    val fatigueLevel: Int = 0,  // 0-100
    
    // Conditions
    val injuries: List<Injury> = emptyList(),
    val illnesses: List<Illness> = emptyList(),
    val mentalHealth: MentalHealthState = MentalHealthState(),
    val fitness: FitnessState = FitnessState(),
    
    // Status effects
    val isBleeding: Boolean = false,
    val isInfected: Boolean = false,
    val isPoisoned: Boolean = false,
    val isParalyzed: Boolean = false,
    val isBlind: Boolean = false,
    val isDeaf: Boolean = false,
    
    // Recovery
    val isHospitalized: Boolean = false,
    val isUnderTreatment: Boolean = false,
    val recoveryRate: Float = 1.0f,
    
    // History
    val medicalHistory: List<String> = emptyList(),
    val allergies: List<String> = emptyList()
) {
    fun getHealthPercentage(): Float = currentHealth.toFloat() / maxHealth.toFloat()
    
    fun isHealthy(): Boolean = currentHealth >= 80 && injuries.isEmpty() && illnesses.isEmpty()
    
    fun isCritical(): Boolean = currentHealth <= 20
    
    fun isDying(): Boolean = currentHealth <= 5
    
    fun getTotalPainLevel(): Int {
        val injuryPain = injuries.sumOf { 
            when (it.severity) {
                InjurySeverity.MINOR -> 5
                InjurySeverity.MODERATE -> 15
                InjurySeverity.SEVERE -> 30
                InjurySeverity.CRITICAL -> 50
                InjurySeverity.LIFE_THREATENING -> 80
            }
        }
        return (painLevel + injuryPain).coerceAtMost(100)
    }
    
    fun getDailyHealthChange(): Int {
        var change = 0
        
        // Illness drain
        change += illnesses.sumOf { it.type.severity.healthDrain }
        
        // Injury impact
        change += injuries.sumOf { it.getHealthImpact() / 10 }
        
        // Mental health impact
        change += mentalHealth.conditions.sumOf { it.severity.statPenalty / 5 }
        
        // Recovery bonus
        if (isUnderTreatment || isHospitalized) {
            change += (recoveryRate * 5).toInt()
        }
        
        return change
    }
    
    fun needsMedicalAttention(): Boolean {
        return isCritical() || 
               injuries.any { it.severity == InjurySeverity.SEVERE || it.severity == InjurySeverity.CRITICAL } ||
               illnesses.any { it.severity == IllnessSeverity.SEVERE || it.severity == IllnessSeverity.CRITICAL }
    }
    
    fun canPerformPhysicalActivity(): Boolean {
        return currentHealth > 50 && 
               !isBleeding && 
               injuries.none { it.bodyPart == BodyPart.LEFT_LEG || it.bodyPart == BodyPart.RIGHT_LEG }
    }
}

/**
 * Health manager for tracking and updating health state
 */
class HealthManager {
    
    private var healthState = HealthState()
    
    fun getState(): HealthState = healthState
    
    fun updateState(newState: HealthState) {
        healthState = newState
    }
    
    fun addInjury(injury: Injury): HealthState {
        healthState = healthState.copy(injuries = healthState.injuries + injury)
        if (injury.isBleeding) {
            healthState = healthState.copy(isBleeding = true)
        }
        return healthState
    }
    
    fun removeInjury(injuryId: String): HealthState {
        healthState = healthState.copy(injuries = healthState.injuries.filter { it.id != injuryId })
        if (healthState.injuries.none { it.isBleeding }) {
            healthState = healthState.copy(isBleeding = false)
        }
        return healthState
    }
    
    fun addIllness(illness: Illness): HealthState {
        healthState = healthState.copy(illnesses = healthState.illnesses + illness)
        return healthState
    }
    
    fun removeIllness(illnessId: String): HealthState {
        healthState = healthState.copy(illnesses = healthState.illnesses.filter { it.id != illnessId })
        return healthState
    }
    
    fun healInjuries(amount: Int): HealthState {
        val healedInjuries = healthState.injuries.map { it.advanceHealing(amount) }
            .filter { !it.isHealed() }
        val removedIds = healthState.injuries.filter { it.isHealed() }.map { it.id }
        
        healthState = healthState.copy(injuries = healedInjuries)
        removedIds.forEach { removeInjury(it) }
        
        return healthState
    }
    
    fun treatIllnesses(amount: Int): HealthState {
        val treatedIllnesses = healthState.illnesses.map { it.advanceTreatment(amount) }
            .filter { !it.isRecovered() }
        val recoveredIds = healthState.illnesses.filter { it.isRecovered() }.map { it.id }
        
        healthState = healthState.copy(illnesses = treatedIllnesses)
        recoveredIds.forEach { removeIllness(it) }
        
        return healthState
    }
    
    fun modifyHealth(amount: Int): HealthState {
        healthState = healthState.copy(
            currentHealth = (healthState.currentHealth + amount).coerceIn(0, healthState.maxHealth)
        )
        return healthState
    }
    
    fun advanceTime(hours: Int): HealthState {
        // Apply illness drain over time
        val healthDrain = healthState.getDailyHealthChange() * hours / 24
        healthState = modifyHealth(healthDrain)
        
        // Natural healing
        if (healthState.isUnderTreatment) {
            healInjuries(hours)
            treatIllnesses(hours)
        }
        
        return healthState
    }
    
    fun checkForInfection(): HealthState {
        val infectedInjuries = healthState.injuries.filter { it.isInfected }
        if (infectedInjuries.isNotEmpty()) {
            healthState = healthState.copy(isInfected = true)
        }
        return healthState
    }
    
    fun applyTreatment(treatmentType: String, effectiveness: Float): HealthState {
        healthState = when (treatmentType) {
            "rest" -> healthState.copy(recoveryRate = healthState.recoveryRate + 0.2f)
            "medicine" -> healthState.copy(recoveryRate = healthState.recoveryRate + 0.3f)
            "surgery" -> healthState.copy(recoveryRate = healthState.recoveryRate + 0.5f)
            "therapy" -> healthState.copy(
                mentalHealth = healthState.mentalHealth.copy(
                    therapyProgress = healthState.mentalHealth.therapyProgress + 10
                )
            )
            else -> healthState
        }
        return healthState
    }
}
