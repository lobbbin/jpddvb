package com.popop.lifesimulator.core.lifepaths

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Career Life Path Manager
 * Handles professional careers across multiple fields: medical, legal, education, corporate, trades, etc.
 */
class CareerManager {
    
    private val _careerState = MutableStateFlow(CareerState())
    val careerState: StateFlow<CareerState> = _careerState.asStateFlow()
    
    // Career Progression
    private var currentJob: Job? = null
    private val jobHistory = mutableListOf<Job>()
    private var careerLevel: Int = 0
    private var experience: Int = 0
    
    // Education
    private val degrees = mutableListOf<Degree>()
    private val certifications = mutableListOf<Certification>()
    private var educationLevel: EducationLevel = EducationLevel.HIGH_SCHOOL
    
    // Skills
    private val professionalSkills = mutableMapOf<String, Int>()
    
    // Work Performance
    private var performanceRating: Int = 50
    private var workStress: Int = 0
    private var workLifeBalance: Int = 50
    
    // Network
    private val contacts = mutableListOf<ProfessionalContact>()
    private val mentors = mutableListOf<Mentor>()
    private val proteges = mutableListOf<Protege>()
    
    // Career-specific states
    private val medicalState = MedicalCareerState()
    private val legalState = LegalCareerState()
    private val educationState = EducationCareerState()
    private val corporateState = CorporateCareerState()
    
    fun initialize(startingEducation: EducationLevel, startingJob: Job?) {
        educationLevel = startingEducation
        currentJob = startingJob
    }
    
    // Job Management
    fun applyForJob(job: Job): ApplicationResult {
        if (!meetsRequirements(job)) {
            return ApplicationResult.NotQualified
        }
        
        val hireChance = calculateHireChance(job)
        
        return if (kotlin.random.Random.nextInt(100) < hireChance) {
            currentJob?.let { jobHistory.add(it) }
            currentJob = job
            experience += job.experienceGain
            careerLevel = calculateCareerLevel()
            
            ApplicationResult.Hired(job)
        } else {
            ApplicationResult.Rejected
        }
    }
    
    fun quitJob(): QuitResult {
        val job = currentJob ?: return QuitResult.Unemployed
        
        jobHistory.add(job)
        currentJob = null
        
        return QuitResult.Success(job.title)
    }
    
    fun askForRaise(): RaiseResult {
        val job = currentJob ?: return RaiseResult.Unemployed
        
        if (performanceRating < 60) {
            return RaiseResult.PoorPerformance
        }
        
        val raiseChance = (performanceRating / 2 + careerLevel).coerceAtMost(80)
        
        return if (kotlin.random.Random.nextInt(100) < raiseChance) {
            val raiseAmount = job.salary * 0.1
            job.salary += raiseAmount
            RaiseResult.Success(raiseAmount)
        } else {
            RaiseResult.Denied
        }
    }
    
    fun requestPromotion(): PromotionResult {
        val job = currentJob ?: return PromotionResult.Unemployed
        
        if (experience < job.requiredExperience * 1.5) {
            return PromotionResult.InsufficientExperience
        }
        
        val promotionChance = (performanceRating + careerLevel).coerceAtMost(85)
        
        return if (kotlin.random.Random.nextInt(100) < promotionChance) {
            val promotedJob = job.copy(
                title = getNextLevelTitle(job.title),
                salary = job.salary * 1.3,
                requiredExperience = job.requiredExperience * 2
            )
            
            currentJob = promotedJob
            PromotionResult.Success(promotedJob)
        } else {
            PromotionResult.Denied
        }
    }
    
    // Work Actions
    fun workOvertime(hours: Int): OvertimeResult {
        val job = currentJob ?: return OvertimeResult.Unemployed
        
        if (workStress > 80) {
            return OvertimeResult.TooStressed
        }
        
        val overtimePay = job.salary * (hours / 160.0) * 1.5
        workStress = (workStress + hours * 2).coerceAtMost(100)
        performanceRating = (performanceRating + 2).coerceAtMost(100)
        
        return OvertimeResult.Success(overtimePay, hours)
    }
    
    fun takeVacation(days: Int): VacationResult {
        if (currentJob == null) {
            return VacationResult.Unemployed
        }
        
        workStress = (workStress - days * 5).coerceAtLeast(0)
        workLifeBalance = (workLifeBalance + days * 3).coerceAtMost(100)
        
        return VacationResult.Success(days)
    }
    
    fun callInSick(): SickResult {
        if (currentJob == null) {
            return SickResult.Unemployed
        }
        
        performanceRating = (performanceRating - 2).coerceAtLeast(0)
        workStress = (workStress - 10).coerceAtLeast(0)
        
        return SickResult.Success
    }
    
    // Education
    fun enrollInDegree(degree: Degree): EnrollmentResult {
        if (degrees.any { it.level == degree.level }) {
            return EnrollmentResult.AlreadyHave
        }
        
        val tuitionCost = degree.tuitionCost
        // Check if player can afford
        
        return EnrollmentResult.Enrolled(degree)
    }
    
    fun completeDegree(degreeId: String): GraduationResult {
        val degree = degrees.find { it.id == degreeId } ?: return GraduationResult.NotFound
        
        if (!degree.isCompleted) {
            return GraduationResult.NotFinished
        }
        
        educationLevel = degree.level
        careerLevel += 10
        
        return GraduationResult.Success(degree)
    }
    
    fun getCertification(certification: Certification): CertificationResult {
        if (certifications.contains(certification)) {
            return CertificationResult.AlreadyHave
        }
        
        certifications.add(certification)
        return CertificationResult.Success(certification)
    }
    
    // Networking
    fun networkAtEvent(eventType: NetworkingEvent): NetworkResult {
        val newContacts = kotlin.random.Random.nextInt(1, 5)
        val qualityContactChance = performanceRating + careerLevel
        
        if (kotlin.random.Random.nextInt(100) < qualityContactChance) {
            val mentor = Mentor(
                name = "Senior Professional",
                field = currentJob?.field ?: "General",
                willingnessToHelp = kotlin.random.Random.nextInt(40, 90)
            )
            mentors.add(mentor)
            return NetworkResult.MentorFound(mentor)
        }
        
        return NetworkResult.ContactsMade(newContacts)
    }
    
    fun findMentor(): MentorshipResult {
        if (mentors.isNotEmpty()) {
            return MentorshipResult.AlreadyHave
        }
        
        val mentorFound = kotlin.random.Random.nextInt(100) < (careerLevel + performanceRating / 2)
        
        return if (mentorFound) {
            val mentor = Mentor(
                name = "Experienced Mentor",
                field = currentJob?.field ?: "General",
                willingnessToHelp = 70
            )
            mentors.add(mentor)
            MentorshipResult.Success(mentor)
        } else {
            MentorshipResult.NotFound
        }
    }
    
    // Career-Specific Actions
    // Medical
    fun performProcedure(difficulty: Int): MedicalProcedureResult {
        val skill = professionalSkills["Surgery"] ?: 50
        val successChance = skill - difficulty + 50
        
        return when {
            successChance >= 80 -> MedicalProcedureResult.Success
            successChance >= 50 -> MedicalProcedureResult.PartialSuccess
            else -> MedicalProcedureResult.Complication
        }
    }
    
    fun diagnosePatient(symptoms: List<String>): DiagnosisResult {
        val skill = professionalSkills["Diagnosis"] ?: 50
        val correctDiagnosis = kotlin.random.Random.nextInt(100) < skill
        
        return if (correctDiagnosis) {
            DiagnosisResult.Correct
        } else {
            DiagnosisResult.Incorrect
        }
    }
    
    // Legal
    fun handleCase(caseType: CaseType, complexity: Int): CaseResult {
        val skill = professionalSkills["Trial Advocacy"] ?: 50
        val winChance = skill - complexity + 50
        
        return when {
            winChance >= 70 -> CaseResult.LandslideVictory
            winChance >= 50 -> CaseResult.Victory
            winChance >= 30 -> CaseResult.Defeat
            else -> CaseResult.LandslideDefeat
        }
    }
    
    fun negotiateSettlement(opposingValue: Int): SettlementResult {
        val skill = professionalSkills["Negotiation"] ?: 50
        
        return if (skill >= opposingValue) {
            SettlementResult.Favorable
        } else {
            SettlementResult.Unfavorable
        }
    }
    
    // Education
    fun teachClass(subject: String, preparationHours: Int): TeachingResult {
        val skill = professionalSkills["Teaching"] ?: 50
        val effectiveness = skill + preparationHours
        
        return when {
            effectiveness >= 80 -> TeachingResult.Excellent
            effectiveness >= 60 -> TeachingResult.Good
            effectiveness >= 40 -> TeachingResult.Adequate
            else -> TeachingResult.Poor
        }
    }
    
    fun gradeAssignments(count: Int): GradingResult {
        val timeRequired = count * 15  // minutes
        workStress = (workStress + count).coerceAtMost(100)
        
        return GradingResult.Completed(count, timeRequired)
    }
    
    // Corporate
    fun leadProject(complexity: Int, teamSize: Int): ProjectResult {
        val skill = professionalSkills["Leadership"] ?: 50
        val successChance = skill + (teamSize * 2) - complexity
        
        return when {
            successChance >= 70 -> ProjectResult.LandslideSuccess
            successChance >= 40 -> ProjectResult.Success
            else -> ProjectResult.Failure
        }
    }
    
    fun givePresentation(audience: Int): PresentationResult {
        val skill = professionalSkills["Public Speaking"] ?: 50
        
        return when {
            skill >= 80 -> PresentationResult.StandingOvation
            skill >= 60 -> PresentationResult.WellReceived
            skill >= 40 -> PresentationResult.Adequate
            else -> PresentationResult.Poor
        }
    }
    
    // Helper Functions
    private fun meetsRequirements(job: Job): Boolean {
        if (educationLevel.ordinal < job.requiredEducation.ordinal) return false
        if (experience < job.requiredExperience) return false
        job.requiredCertifications.forEach { cert ->
            if (certifications.none { it.name == cert }) return false
        }
        return true
    }
    
    private fun calculateHireChance(job: Job): Int {
        var chance = 50
        chance += (performanceRating - 50) / 2
        chance += (experience - job.requiredExperience).coerceAtMost(20)
        chance += careerLevel
        return chance.coerceIn(10, 90)
    }
    
    private fun calculateCareerLevel(): Int {
        return (experience / 100) + (degrees.size * 10) + (certifications.size * 5)
    }
    
    private fun getNextLevelTitle(currentTitle: String): String {
        val hierarchy = mapOf(
            "Intern" to "Junior",
            "Junior" to "",
            "" to "Senior",
            "Senior" to "Lead",
            "Lead" to "Manager",
            "Manager" to "Director",
            "Director" to "VP",
            "VP" to "SVP",
            "SVP" to "C-Level"
        )
        
        for ((current, next) in hierarchy) {
            if (currentTitle.contains(current)) {
                return currentTitle.replace(current, next).ifEmpty { next }
            }
        }
        
        return "Senior $currentTitle"
    }
    
    fun updateState(newState: CareerState) {
        _careerState.value = newState.copy(
            currentJobTitle = currentJob?.title ?: "Unemployed",
            currentSalary = currentJob?.salary ?: 0.0,
            careerLevel = careerLevel,
            experience = experience,
            educationLevel = educationLevel,
            performanceRating = performanceRating,
            workStress = workStress
        )
    }
    
    fun getState(): CareerState = _careerState.value
}

// Career State
data class CareerState(
    val currentJobTitle: String = "Unemployed",
    val currentSalary: Double = 0.0,
    val careerLevel: Int = 0,
    val experience: Int = 0,
    val educationLevel: EducationLevel = EducationLevel.HIGH_SCHOOL,
    val performanceRating: Int = 50,
    val workStress: Int = 0,
    val workLifeBalance: Int = 50,
    val isEmployed: Boolean = false,
    val yearsInField: Int = 0
)

enum class EducationLevel {
    HIGH_SCHOOL,
    ASSOCIATES,
    BACHELORS,
    MASTERS,
    DOCTORATE,
    PROFESSIONAL
}

// Job Entity
data class Job(
    val id: String,
    val title: String,
    val field: CareerField,
    val salary: Double,
    val requiredEducation: EducationLevel,
    val requiredExperience: Int,
    val requiredCertifications: List<String> = emptyList(),
    val experienceGain: Int = 10,
    val stressLevel: Int = 50,
    val workLifeBalance: Int = 50,
    val location: String = "Remote"
)

enum class CareerField {
    MEDICAL,
    LEGAL,
    EDUCATION,
    CORPORATE,
    TRADES,
    CREATIVE,
    GOVERNMENT,
    MILITARY,
    SCIENCE,
    SERVICE
}

// Education Entities
data class Degree(
    val id: String,
    val name: String,
    val level: EducationLevel,
    val field: String,
    val tuitionCost: Double,
    val durationMonths: Int,
    var isCompleted: Boolean = false
)

data class Certification(
    val id: String,
    val name: String,
    val field: String,
    val cost: Double,
    val validityMonths: Int = -1  // -1 for lifetime
)

// Professional Network
data class ProfessionalContact(
    val id: String,
    val name: String,
    val company: String,
    val position: String,
    val relationshipStrength: Int = 50
)

data class Mentor(
    val name: String,
    val field: String,
    val willingnessToHelp: Int
)

data class Protege(
    val name: String,
    val field: String,
    val potential: Int
)

// Career-Specific States
data class MedicalCareerState(
    val specialty: String? = null,
    val licenseStatus: LicenseStatus = LicenseStatus.NONE,
    val malpracticeClaims: Int = 0,
    val patientsTreated: Int = 0,
    val surgeriesPerformed: Int = 0
)

enum class LicenseStatus {
    NONE,
    STUDENT,
    RESIDENT,
    ATTENDING,
    BOARD_CERTIFIED
}

data class LegalCareerState(
    val barAdmission: String? = null,
    val practiceArea: String? = null,
    val casesWon: Int = 0,
    val casesLost: Int = 0,
    val billableHours: Int = 0
)

data class EducationCareerState(
    val subject: String? = null,
    val gradeLevel: String? = null,
    val tenure: Boolean = false,
    val studentsTaught: Int = 0,
    val publications: Int = 0
)

data class CorporateCareerState(
    val department: String? = null,
    val directReports: Int = 0,
    val projectsLed: Int = 0,
    val bonusesEarned: Double = 0.0
)

// Results
sealed class ApplicationResult {
    data class Hired(val job: Job) : ApplicationResult()
    object NotQualified : ApplicationResult()
    object Rejected : ApplicationResult()
}

sealed class QuitResult {
    data class Success(val jobTitle: String) : QuitResult()
    object Unemployed : QuitResult()
}

sealed class RaiseResult {
    data class Success(val amount: Double) : RaiseResult()
    object PoorPerformance : RaiseResult()
    object Denied : RaiseResult()
    object Unemployed : RaiseResult()
}

sealed class PromotionResult {
    data class Success(val newJob: Job) : PromotionResult()
    object InsufficientExperience : PromotionResult()
    object Denied : PromotionResult()
    object Unemployed : PromotionResult()
}

sealed class OvertimeResult {
    data class Success(val pay: Double, val hours: Int) : OvertimeResult()
    object TooStressed : OvertimeResult()
    object Unemployed : OvertimeResult()
}

sealed class VacationResult {
    data class Success(val days: Int) : VacationResult()
    object Unemployed : VacationResult()
}

sealed class SickResult {
    object Success : SickResult()
    object Unemployed : SickResult()
}

sealed class EnrollmentResult {
    data class Enrolled(val degree: Degree) : EnrollmentResult()
    object AlreadyHave : EnrollmentResult()
    object InsufficientFunds : EnrollmentResult()
}

sealed class GraduationResult {
    data class Success(val degree: Degree) : GraduationResult()
    object NotFinished : GraduationResult()
    object NotFound : GraduationResult()
}

sealed class CertificationResult {
    data class Success(val certification: Certification) : CertificationResult()
    object AlreadyHave : CertificationResult()
}

sealed class NetworkResult {
    data class ContactsMade(val count: Int) : NetworkResult()
    data class MentorFound(val mentor: Mentor) : NetworkResult()
}

sealed class MentorshipResult {
    data class Success(val mentor: Mentor) : MentorshipResult()
    object AlreadyHave : MentorshipResult()
    object NotFound : MentorshipResult()
}

sealed class MedicalProcedureResult {
    object Success : MedicalProcedureResult()
    object PartialSuccess : MedicalProcedureResult()
    object Complication : MedicalProcedureResult()
}

sealed class DiagnosisResult {
    object Correct : DiagnosisResult()
    object Incorrect : DiagnosisResult()
}

enum class CaseType {
    CRIMINAL,
    CIVIL,
    CORPORATE,
    FAMILY,
    CONSTITUTIONAL
}

sealed class CaseResult {
    object LandslideVictory : CaseResult()
    object Victory : CaseResult()
    object Defeat : CaseResult()
    object LandslideDefeat : CaseResult()
}

sealed class SettlementResult {
    object Favorable : SettlementResult()
    object Unfavorable : SettlementResult()
}

sealed class TeachingResult {
    object Excellent : TeachingResult()
    object Good : TeachingResult()
    object Adequate : TeachingResult()
    object Poor : TeachingResult()
}

sealed class GradingResult {
    data class Completed(val count: Int, val timeMinutes: Int) : GradingResult()
}

sealed class ProjectResult {
    object LandslideSuccess : ProjectResult()
    object Success : ProjectResult()
    object Failure : ProjectResult()
}

sealed class PresentationResult {
    object StandingOvation : PresentationResult()
    object WellReceived : PresentationResult()
    object Adequate : PresentationResult()
    object Poor : PresentationResult()
}
