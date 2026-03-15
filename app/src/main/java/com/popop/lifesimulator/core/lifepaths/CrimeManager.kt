package com.popop.lifesimulator.core.lifepaths

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Crime Life Path Manager
 * Handles criminal enterprises, heists, turf wars, prison mechanics
 */
class CrimeManager {
    
    private val _crimeState = MutableStateFlow(CrimeState())
    val crimeState: StateFlow<CrimeState> = _crimeState.asStateFlow()
    
    // Criminal Enterprise
    private val crewMembers = mutableListOf<CrewMember>()
    private var turfControl: Int = 0
    private var streetCred: Int = 0
    private var heatLevel: Int = 0
    
    // Operations
    private val activeOperations = mutableListOf<CriminalOperation>()
    private val completedHeists = mutableListOf<HeistResult>()
    
    // Connections
    private val fences = mutableListOf<Fence>()
    private val corruptContacts = mutableListOf<CorruptContact>()
    
    // Prison (if incarcerated)
    private var isImprisoned: Boolean = false
    private var prisonSentence: Int = 0  // months
    private var timeServed: Int = 0
    private val prisonGangAffiliation: String? = null
    private val prisonReputation: Int = 50
    
    fun initialize(startingCred: Int, startingHeat: Int) {
        streetCred = startingCred
        heatLevel = startingHeat
    }
    
    // Crew Management
    fun recruitCrewMember(member: CrewMember): RecruitmentResult {
        if (crewMembers.size >= 10) {
            return RecruitmentResult.CrewFull
        }
        
        if (crewMembers.any { it.name == member.name }) {
            return RecruitmentResult.AlreadyInCrew
        }
        
        crewMembers.add(member)
        return RecruitmentResult.Success(member)
    }
    
    fun removeCrewMember(memberName: String): RemovalResult {
        val member = crewMembers.find { it.name == memberName }
        if (member == null) {
            return RemovalResult.NotFound
        }
        
        if (member.loyalty < 30) {
            // Risk of betrayal
            if (kotlin.random.Random.nextInt(100) < 30) {
                heatLevel += 20
                return RemovalResult.Betrayal(memberName)
            }
        }
        
        crewMembers.removeAll { it.name == memberName }
        return RemovalResult.Success(memberName)
    }
    
    // Heist Planning and Execution
    fun planHeist(target: HeistTarget, crew: List<String>, preparationDays: Int): HeistPlanResult {
        val selectedCrew = crewMembers.filter { crew.contains(it.name) }
        
        if (selectedCrew.isEmpty()) {
            return HeistPlanResult.NoCrew
        }
        
        val crewSkill = selectedCrew.map { it.skill }.average()
        val difficulty = target.difficulty
        
        val successChance = ((crewSkill - difficulty) + (preparationDays * 2)).coerceIn(10, 90)
        
        return HeistPlanResult.Success(successChance.toInt(), target, selectedCrew)
    }
    
    fun executeHeist(plan: HeistPlanResult.HeistPlan): HeistExecutionResult {
        if (plan !is HeistPlanResult.Success) {
            return HeistExecutionResult.InvalidPlan
        }
        
        val crewSkill = plan.crew.map { it.skill }.average()
        val difficulty = plan.target.difficulty
        val roll = kotlin.random.Random.nextInt(100) + (crewSkill - difficulty).toInt()
        
        return when {
            roll >= 80 -> {
                val take = plan.target.maxTake * (roll / 100.0)
                heatLevel += 15
                streetCred += 25
                completedHeists.add(HeistResult.Success(plan.target, take))
                HeistExecutionResult.Success(take, roll)
            }
            roll >= 50 -> {
                val take = plan.target.maxTake * 0.5
                heatLevel += 10
                completedHeists.add(HeistResult.PartialSuccess(plan.target, take))
                HeistExecutionResult.PartialSuccess(take, roll)
            }
            else -> {
                heatLevel += 30
                streetCred -= 10
                completedHeists.add(HeistResult.Failure(plan.target))
                HeistExecutionResult.Failure(roll)
            }
        }
    }
    
    // Turf Management
    fun claimTurf(area: String, rivalGang: String?): TurfResult {
        if (rivalGang != null) {
            // Turf war initiated
            return initiateTurfWar(rivalGang, area)
        }
        
        turfControl += 5
        return TurfResult.Success(area, false)
    }
    
    private fun initiateTurfWar(rivalGang: String, area: String): TurfResult {
        // Simplified turf war mechanics
        val ourStrength = crewMembers.sumOf { it.skill } + streetCred
        val rivalStrength = kotlin.random.Random.nextInt(50, 150)
        
        return if (ourStrength >= rivalStrength) {
            turfControl += 10
            streetCred += 15
            heatLevel += 20
            TurfResult.WarVictory(area, rivalGang)
        } else {
            streetCred -= 10
            crewMembers.filter { it.skill < 30 }.forEach {
                it.loyalty -= 10  // Some crew may leave after defeat
            }
            TurfResult.WarDefeat(area, rivalGang)
        }
    }
    
    fun defendTurf(attacker: String): DefenseResult {
        val defenseStrength = crewMembers.sumOf { it.skill } + (turfControl * 2)
        val attackStrength = kotlin.random.Random.nextInt(50, 150)
        
        return if (defenseStrength >= attackStrength) {
            streetCred += 10
            DefenseResult.Success(attacker)
        } else {
            turfControl -= 5
            heatLevel += 10
            DefenseResult.Lost(attacker)
        }
    }
    
    // Illegal Operations
    fun startDrugOperation(type: DrugType, location: String): OperationResult {
        if (heatLevel > 70) {
            return OperationResult.TooMuchHeat
        }
        
        val operation = CriminalOperation(
            type = OperationType.DRUGS,
            subtype = type.name,
            location = location,
            profitPerDay = type.profitMultiplier * 100,
            riskLevel = type.riskLevel
        )
        
        activeOperations.add(operation)
        heatLevel += 5
        
        return OperationResult.Success(operation)
    }
    
    fun launderMoney(amount: Double, fence: Fence): LaunderResult {
        val fee = amount * fence.feePercentage
        val launderedAmount = amount - fee
        
        if (kotlin.random.Random.nextInt(100) < fence.reliability) {
            heatLevel -= 5
            return LaunderResult.Success(launderedAmount, fee)
        } else {
            // Fence might be unreliable
            if (kotlin.random.Random.nextInt(100) > fence.reliability) {
                heatLevel += 20
                return LaunderResult.Betrayal(fee)
            }
            return LaunderResult.Success(launderedAmount, fee)
        }
    }
    
    // Police Interactions
    fun getArrested(evidence: Int): ArrestResult {
        val arrestChance = (evidence + heatLevel).coerceAtMost(95)
        
        return if (kotlin.random.Random.nextInt(100) < arrestChance) {
            isImprisoned = true
            prisonSentence = (evidence / 10).coerceIn(6, 120)  // 6 months to 10 years
            timeServed = 0
            ArrestResult.Caught(prisonSentence)
        } else {
            heatLevel -= 10  // Laying low after close call
            ArrestResult.Escaped
        }
    }
    
    fun postBail(bailAmount: Double): BailResult {
        if (bailAmount > 100000) {
            return BailResult.TooHigh
        }
        
        // Assume player can pay bail for simplicity
        isImprisoned = false
        heatLevel += 5
        return BailResult.Success(bailAmount)
    }
    
    // Prison Mechanics
    fun serveTime(months: Int): PrisonTimeResult {
        timeServed += months
        
        if (timeServed >= prisonSentence) {
            isImprisoned = false
            return PrisonTimeResult.Released
        }
        
        // Prison events can happen here
        return PrisonTimeResult.Continued(timeServed, prisonSentence - timeServed)
    }
    
    fun joinPrisonGang(gangName: String): GangResult {
        if (prisonGangAffiliation != null) {
            return GangResult.AlreadyMember
        }
        
        prisonReputation += 20
        return GangResult.Joined(gangName)
    }
    
    fun prisonFight(opponent: String): FightResult {
        val playerSkill = streetCred + crewMembers.count { it.combatSkill > 50 } * 10
        val opponentSkill = kotlin.random.Random.nextInt(30, 80)
        
        return when {
            playerSkill >= opponentSkill + 20 -> FightResult.DominantVictory
            playerSkill >= opponentSkill -> FightResult.Victory
            playerSkill >= opponentSkill - 20 -> FightResult.CloseDefeat
            else -> FightResult.Defeat
        }
    }
    
    // Parole
    fun applyForParole(): ParoleResult {
        if (timeServed < prisonSentence / 2) {
            return ParoleResult.TooEarly
        }
        
        val paroleChance = (prisonReputation + (timeServed * 2) - heatLevel).coerceIn(10, 90)
        
        return if (kotlin.random.Random.nextInt(100) < paroleChance) {
            isImprisoned = false
            ParoleResult.Granted
        } else {
            ParoleResult.Denied
        }
    }
    
    fun updateState(newState: CrimeState) {
        _crimeState.value = newState.copy(
            turfControl = turfControl,
            streetCred = streetCred,
            heatLevel = heatLevel,
            isImprisoned = isImprisoned,
            crewSize = crewMembers.size
        )
    }
    
    fun getState(): CrimeState = _crimeState.value
    fun getCrewMembers(): List<CrewMember> = crewMembers.toList()
    fun getActiveOperations(): List<CriminalOperation> = activeOperations.toList()
}

// Crime State
data class CrimeState(
    val organizationName: String = "",
    val organizationTier: CrimeTier = CrimeTier.STREET,
    val turfControl: Int = 0,
    val streetCred: Int = 0,
    val heatLevel: Int = 0,
    val isImprisoned: Boolean = false,
    val prisonSentence: Int = 0,
    val timeServed: Int = 0,
    val crewSize: Int = 0,
    val activeWarrants: Int = 0,
    val moneyLaundered: Double = 0.0
)

enum class CrimeTier(val displayName: String, val minCred: Int) {
    STREET("Street Criminal", 0),
    THUG("Thug", 20),
    GANGSTER("Gangster", 40),
    ENFORCER("Enforcer", 60),
    LIEUTENANT("Lieutenant", 75),
    BOSS("Crime Boss", 90),
    KINGPIN("Crime Kingpin", 95)
}

// Crew Members
data class CrewMember(
    val name: String,
    val role: CrewRole,
    val skill: Int,
    val loyalty: Int,
    val combatSkill: Int = 50,
    val specialty: String? = null,
    val cutPercentage: Float = 10f,
    val isInformant: Boolean = false
)

enum class CrewRole {
    HACKER,
    DRIVER,
    MUSCLE,
    LOCKSMITH,
    DEMOLITIONS,
    LOOKOUT,
    FORGER,
    HITMAN,
    SECOND_IN_COMMAND
}

// Heists
data class HeistTarget(
    val name: String,
    val type: HeistType,
    val difficulty: Int,
    val maxTake: Double,
    val securityLevel: Int,
    val description: String
)

enum class HeistType {
    CONVENIENCE_STORE,
    BANK,
    JEWELRY_STORE,
    MUSEUM,
    CASINO,
    ARMORED_TRUCK,
    WAREHOUSE,
    PRIVATE_RESIDENCE
}

sealed class HeistPlanResult {
    data class Success(val chance: Int, val target: HeistTarget, val crew: List<CrewMember>) : HeistPlanResult()
    object NoCrew : HeistPlanResult()
    object InsufficientSkill : HeistPlanResult()
}

sealed class HeistExecutionResult {
    data class Success(val take: Double, val roll: Int) : HeistExecutionResult()
    data class PartialSuccess(val take: Double, val roll: Int) : HeistExecutionResult()
    data class Failure(val roll: Int) : HeistExecutionResult()
    object InvalidPlan : HeistExecutionResult()
    object Caught : HeistExecutionResult()
}

data class HeistResult {
    data class Success(val target: HeistTarget, val take: Double) : HeistResult()
    data class PartialSuccess(val target: HeistTarget, val take: Double) : HeistResult()
    data class Failure(val target: HeistTarget) : HeistResult()
}

// Operations
data class CriminalOperation(
    val type: OperationType,
    val subtype: String,
    val location: String,
    val profitPerDay: Double,
    val riskLevel: Int,
    val isUnderInvestigation: Boolean = false
)

enum class OperationType {
    DRUGS,
    GAMBLING,
    PROSTITUTION,
    EXTORTION,
    SMUGGLING,
    COUNTERFEITING
}

data class DrugType(val profitMultiplier: Double, val riskLevel: Int) {
    companion object {
        val WEED = DrugType(1.0, 20)
        val COCAINE = DrugType(3.0, 50)
        val HEROIN = DrugType(4.0, 70)
        val METH = DrugType(3.5, 60)
        val PRESCRIPTION = DrugType(2.0, 40)
    }
}

// Fences and Contacts
data class Fence(
    val name: String,
    val feePercentage: Double,
    val reliability: Int,
    val specialty: String? = null
)

data class CorruptContact(
    val name: String,
    val position: String,
    val bribeCost: Double,
    val usefulness: Int
)

// Results
sealed class RecruitmentResult {
    data class Success(val member: CrewMember) : RecruitmentResult()
    object CrewFull : RecruitmentResult()
    object AlreadyInCrew : RecruitmentResult()
    object Declined : RecruitmentResult()
}

sealed class RemovalResult {
    data class Success(val memberName: String) : RemovalResult()
    object NotFound : RemovalResult()
    data class Betrayal(val memberName: String) : RemovalResult()
}

sealed class TurfResult {
    data class Success(val area: String, val isContested: Boolean) : TurfResult()
    data class WarVictory(val area: String, val rivalGang: String) : TurfResult()
    data class WarDefeat(val area: String, val rivalGang: String) : TurfResult()
    object AlreadyControlled : TurfResult()
}

sealed class DefenseResult {
    data class Success(val attacker: String) : DefenseResult()
    data class Lost(val attacker: String) : DefenseResult()
}

sealed class OperationResult {
    data class Success(val operation: CriminalOperation) : OperationResult()
    object TooMuchHeat : OperationResult()
    object TerritoryNeeded : OperationResult()
}

sealed class LaunderResult {
    data class Success(val amount: Double, val fee: Double) : LaunderResult()
    data class Betrayal(val lostAmount: Double) : LaunderResult()
}

sealed class ArrestResult {
    data class Caught(val sentenceMonths: Int) : ArrestResult()
    object Escaped : ArrestResult()
}

sealed class BailResult {
    data class Success(val amount: Double) : BailResult()
    object TooHigh : BailResult()
    object Denied : BailResult()
}

sealed class PrisonTimeResult {
    object Released : PrisonTimeResult()
    data class Continued(val served: Int, val remaining: Int) : PrisonTimeResult()
}

sealed class GangResult {
    data class Joined(val gangName: String) : GangResult()
    object AlreadyMember : GangResult()
    object Rejected : GangResult()
}

enum class FightResult {
    DOMINANT_VICTORY,
    VICTORY,
    CLOSE_DEFEAT,
    DEFEAT
}

sealed class ParoleResult {
    object Granted : ParoleResult()
    object Denied : ParoleResult()
    object TooEarly : ParoleResult()
}
