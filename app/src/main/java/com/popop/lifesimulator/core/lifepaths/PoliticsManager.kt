package com.popop.lifesimulator.core.lifepaths

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Politics Life Path Manager
 * Handles elections, legislation, campaigns, and political influence
 */
class PoliticsManager {
    
    private val _politicsState = MutableStateFlow(PoliticsState())
    val politicsState: StateFlow<PoliticsState> = _politicsState.asStateFlow()
    
    // Campaign
    private var campaignFunds: Double = 0.0
    private val donors = mutableListOf<PoliticalDonor>()
    private val campaignStaff = mutableListOf<CampaignStaff>()
    private var pollingNumbers: Int = 50
    private var momentum: Int = 0
    
    // Governance
    private var billsIntroduced: Int = 0
    private var billsPassed: Int = 0
    private var vetoesIssued: Int = 0
    private var approvalRating: Int = 50
    
    // Party
    private var partySupport: Int = 50
    private var factionSupport: Map<String, Int> = emptyMap()
    
    // Media
    private var mediaCoverage: MediaSentiment = MediaSentiment.NEUTRAL
    private val endorsements = mutableListOf<Endorsement>()
    
    fun initialize(startingFunds: Double) {
        campaignFunds = startingFunds
        pollingNumbers = 50
        approvalRating = 50
    }
    
    // Campaign Actions
    fun holdRally(location: String, budget: Double): RallyResult {
        if (campaignFunds < budget) {
            return RallyResult.InsufficientFunds
        }
        
        campaignFunds -= budget
        
        // Calculate rally effectiveness based on budget and location
        val effectiveness = (budget / 10000).toInt().coerceIn(5, 30)
        pollingNumbers = (pollingNumbers + effectiveness).coerceAtMost(100)
        momentum = (momentum + 5).coerceAtMost(100)
        
        return RallyResult.Success(effectiveness, pollingNumbers)
    }
    
    fun runAdCampaign(type: AdType, budget: Double, targetDemographic: String): AdResult {
        if (campaignFunds < budget) {
            return AdResult.InsufficientFunds
        }
        
        campaignFunds -= budget
        
        val impact = when (type) {
            AdType.POSITIVE -> 10
            AdType.NEGATIVE -> 15
            AdType.BIOGRAPHICAL -> 8
        }
        
        pollingNumbers = (pollingNumbers + impact).coerceAtMost(100)
        
        return AdResult.Success(impact, type)
    }
    
    fun debate(debateType: DebateType, preparationDays: Int): DebateResult {
        // Performance based on preparation and skills
        val preparationBonus = preparationDays * 3
        val basePerformance = 50 + preparationBonus
        
        return when {
            basePerformance >= 80 -> DebateResult.LandslideVictory
            basePerformance >= 65 -> DebateResult.Victory
            basePerformance >= 45 -> DebateResult.Draw
            basePerformance >= 30 -> DebateResult.Defeat
            else -> DebateResult.LandslideDefeat
        }
    }
    
    fun solicitDonation(donor: PoliticalDonor): DonationResult {
        if (donors.contains(donor)) {
            return DonationResult.AlreadyDonated
        }
        
        donors.add(donor)
        campaignFunds += donor.amount
        
        // Donors may have expectations
        if (donor.hasExpectations) {
            // Track donor expectations for future events
        }
        
        return DonationResult.Success(donor.amount)
    }
    
    // Governance Actions
    fun introduceBill(title: String, description: String, support: Int): BillResult {
        billsIntroduced++
        
        val passChance = support + approvalRating + (partySupport / 2)
        val passed = passChance >= 50
        
        if (passed) {
            billsPassed++
            approvalRating = (approvalRating + 5).coerceAtMost(100)
            return BillResult.Passed(title)
        } else {
            return BillResult.Failed(title, "Insufficient support")
        }
    }
    
    fun vetoBill(billName: String): VetoResult {
        vetoesIssued++
        approvalRating = (approvalRating - 2).coerceAtLeast(0)
        return VetoResult.Success(billName)
    }
    
    fun executiveOrder(title: String, description: String): ExecutiveOrderResult {
        // Executive orders bypass legislature but may face legal challenges
        approvalRating = (approvalRating - 3).coerceAtLeast(0)
        return ExecutiveOrderResult.Success(title)
    }
    
    // Media & Public Relations
    fun holdPressConference(topic: String, tone: PressTone): PressResult {
        return when (tone) {
            PressTone.CONFIDENT -> {
                mediaCoverage = MediaSentiment.POSITIVE
                approvalRating = (approvalRating + 5).coerceAtMost(100)
                PressResult.Success("Confident delivery well-received")
            }
            PressTone.DEFENSIVE -> {
                mediaCoverage = MediaSentiment.NEGATIVE
                approvalRating = (approvalRating - 5).coerceAtLeast(0)
                PressResult.Success("Defensive tone noted by press")
            }
            PressTone.FORTHRIGHT -> {
                mediaCoverage = MediaSentiment.POSITIVE
                approvalRating = (approvalRating + 8).coerceAtMost(100)
                PressResult.Success("Honesty appreciated")
            }
        }
    }
    
    fun respondToScandal(responseType: ScandalResponse): ScandalResult {
        return when (responseType) {
            ScandalResponse.DENY -> {
                if (kotlin.random.Random.nextBoolean()) {
                    ScandalResult.Success("Denial believed")
                } else {
                    ScandalResult.Failure("Denial not believed")
                }
            }
            ScandalResponse.APOLOGIZE -> {
                approvalRating = (approvalRating - 10).coerceAtLeast(0)
                ScandalResult.Success("Apology accepted by public")
            }
            ScandalResponse.DEFLECT -> {
                if (kotlin.random.Random.nextBoolean()) {
                    ScandalResult.Success("Attention diverted")
                } else {
                    ScandalResult.Failure("Deflection seen as evasion")
                }
            }
            ScandalResponse.IGNORE -> {
                ScandalResult.Failure("Story grows without response")
            }
        }
    }
    
    // Party Management
    fun secureEndorsement(endorser: Endorsement): EndorsementResult {
        if (endorsements.contains(endorser)) {
            return EndorsementResult.AlreadyEndorsed
        }
        
        endorsements.add(endorser)
        pollingNumbers = (pollingNumbers + endorser.impact).coerceAtMost(100)
        
        return EndorsementResult.Success(endorser)
    }
    
    fun negotiateWithFaction(factionName: String, concession: String): FactionResult {
        val currentSupport = factionSupport[factionName] ?: 50
        val newSupport = (currentSupport + 15).coerceAtMost(100)
        factionSupport = factionSupport + (factionName to newSupport)
        
        return FactionResult.Success(factionName, newSupport)
    }
    
    // Election
    fun holdElection(): ElectionResult {
        val finalPoll = pollingNumbers + (momentum / 2) + (kotlin.random.Random.nextInt(-10, 10))
        
        return when {
            finalPoll >= 55 -> ElectionResult.LandslideVictory(finalPoll)
            finalPoll >= 50 -> ElectionResult.Victory(finalPoll)
            finalPoll >= 45 -> ElectionResult.CloseDefeat(finalPoll)
            else -> ElectionResult.Defeat(finalPoll)
        }
    }
    
    fun updateState(newState: PoliticsState) {
        _politicsState.value = newState.copy(
            campaignFunds = campaignFunds,
            pollingNumbers = pollingNumbers,
            approvalRating = approvalRating,
            billsPassed = billsPassed
        )
    }
    
    fun getState(): PoliticsState = _politicsState.value
}

// Politics State
data class PoliticsState(
    val currentPosition: PoliticalPosition = PoliticalPosition.NONE,
    val termStart: Long = 0,
    val termEnd: Long = 0,
    val campaignFunds: Double = 0.0,
    val pollingNumbers: Int = 50,
    val approvalRating: Int = 50,
    val momentum: Int = 0,
    val billsPassed: Int = 0,
    val scandalsActive: Int = 0,
    val endorsementsCount: Int = 0
)

enum class PoliticalPosition(val displayName: String) {
    NONE("None"),
    CITY_COUNCIL("City Council"),
    MAYOR("Mayor"),
    STATE_REPRESENTATIVE("State Representative"),
    STATE_SENATOR("State Senator"),
    GOVERNOR("Governor"),
    US_REPRESENTATIVE("US Representative"),
    US_SENATOR("US Senator"),
    PRESIDENT("President")
}

// Campaign Entities
data class PoliticalDonor(
    val name: String,
    val amount: Double,
    val type: DonorType,
    val hasExpectations: Boolean = true,
    val expectations: String? = null
)

enum class DonorType {
    INDIVIDUAL,
    CORPORATION,
    PAC,
    SUPER_PAC,
    UNION,
    PARTY
}

data class CampaignStaff(
    val name: String,
    val role: StaffRole,
    val effectiveness: Int,
    val salary: Double
)

enum class StaffRole {
    CAMPAIGN_MANAGER,
    COMMUNICATIONS_DIRECTOR,
    FIELD_ORGANIZER,
    DIGITAL_DIRECTOR,
    FINANCE_DIRECTOR,
    POLICY_ADVISOR
}

// Bills and Legislation
data class Bill(
    val id: String,
    val title: String,
    val description: String,
    val sponsor: String,
    val coSponsors: List<String> = emptyList(),
    val committee: String? = null,
    val status: BillStatus = BillStatus.INTRODUCED,
    val support: Int = 50,
    val opposition: Int = 0
)

enum class BillStatus {
    INTRODUCED,
    IN_COMMITTEE,
    ON_FLOOR,
    PASSED_SENATE,
    PASSED_HOUSE,
    PASSED_BOTH,
    VETOED,
    ENACTED,
    FAILED
}

// Media and Endorsements
enum class MediaSentiment {
    VERY_NEGATIVE,
    NEGATIVE,
    NEUTRAL,
    POSITIVE,
    VERY_POSITIVE
}

data class Endorsement(
    val endorser: String,
    val type: EndorsementType,
    val impact: Int,
    val credibility: Int
)

enum class EndorsementType {
    NEWSPAPER,
    UNION,
    CELEBRITY,
    POLITICIAN,
    ORGANIZATION,
    RELIGIOUS_LEADER
}

// Results
sealed class RallyResult {
    data class Success(val attendance: Int, val newPolling: Int) : RallyResult()
    object InsufficientFunds : RallyResult()
}

enum class AdType {
    POSITIVE,
    NEGATIVE,
    BIOGRAPHICAL
}

sealed class AdResult {
    data class Success(val impact: Int, val type: AdType) : AdResult()
    object InsufficientFunds : AdResult()
}

enum class DebateType {
    PRIMARY,
    GENERAL,
    TOWN_HALL,
    ONE_ON_ONE
}

enum class DebateResult {
    LANDSLIDE_VICTORY,
    VICTORY,
    DRAW,
    DEFEAT,
    LANDSLIDE_DEFEAT
}

sealed class DonationResult {
    data class Success(val amount: Double) : DonationResult()
    object AlreadyDonated : DonationResult()
    object Declined : DonationResult()
}

sealed class BillResult {
    data class Passed(val title: String) : BillResult()
    data class Failed(val title: String, val reason: String) : BillResult()
}

sealed class VetoResult {
    data class Success(val billName: String) : VetoResult()
    object Overridden : VetoResult()
}

sealed class ExecutiveOrderResult {
    data class Success(val title: String) : ExecutiveOrderResult()
    data class Blocked(val reason: String) : ExecutiveOrderResult()
}

enum class PressTone {
    CONFIDENT,
    DEFENSIVE,
    FORTHRIGHT,
    EVASIVE
}

sealed class PressResult {
    data class Success(val message: String) : PressResult()
    data class Failure(val message: String) : PressResult()
}

enum class ScandalResponse {
    DENY,
    APOLOGIZE,
    DEFLECT,
    IGNORE
}

sealed class ScandalResult {
    data class Success(val message: String) : ScandalResult()
    data class Failure(val message: String) : ScandalResult()
}

sealed class FactionResult {
    data class Success(val factionName: String, val support: Int) : FactionResult()
    data class Failure(val reason: String) : FactionResult()
}

sealed class EndorsementResult {
    data class Success(val endorser: Endorsement) : EndorsementResult()
    object AlreadyEndorsed : EndorsementResult()
    object Declined : EndorsementResult()
}

sealed class ElectionResult {
    data class LandslideVictory(val percentage: Int) : ElectionResult()
    data class Victory(val percentage: Int) : ElectionResult()
    data class CloseDefeat(val percentage: Int) : ElectionResult()
    data class Defeat(val percentage: Int) : ElectionResult()
}
