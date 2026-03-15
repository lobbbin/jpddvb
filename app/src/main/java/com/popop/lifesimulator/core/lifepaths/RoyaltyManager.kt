package com.popop.lifesimulator.core.lifepaths

import com.popop.lifesimulator.data.models.character.Character
import com.popop.lifesimulator.data.models.world.Faction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Royalty Life Path Manager
 * Handles all royalty-specific mechanics: court management, realm management, diplomacy, military
 */
class RoyaltyManager {
    
    private val _royaltyState = MutableStateFlow(RoyaltyState())
    val royaltyState: StateFlow<RoyaltyState> = _royaltyState.asStateFlow()
    
    // Court Management
    private val courtiers = mutableListOf<Courtier>()
    private val councilMembers = mutableListOf<CouncilMember>()
    
    // Realm Management
    private val provinces = mutableListOf<Province>()
    private var treasury: Double = 0.0
    private var royalDemense: Int = 0
    
    // Diplomacy
    private val foreignRelations = mutableMapOf<String, ForeignRelation>()
    private val alliances = mutableListOf<Alliance>()
    private val wars = mutableListOf<War>()
    
    // Military
    private var armySize: Int = 0
    private var navySize: Int = 0
    private var morale: Int = 50
    
    fun initialize(character: Character) {
        treasury = character.wealth
        armySize = 1000
        navySize = 100
        morale = 50
        
        // Initialize starting courtiers
        courtiers.addAll(listOf(
            Courtier("Grand Vizier", "Chief Advisor", 70, 80),
            Courtier("Master of Coin", "Treasury Manager", 50, 60),
            Courtier("Lord Commander", "Military Leader", 60, 70)
        ))
    }
    
    fun collectTaxes(): Double {
        val baseTax = provinces.sumOf { it.taxRevenue }
        val collection = baseTax * (0.5 + (councilMembers.find { it.position == "Master of Coin" }?.effectiveness ?: 0.5))
        treasury += collection
        return collection
    }
    
    fun holdCourt(action: CourtAction): CourtResult {
        return when (action) {
            is CourtAction.GrantTitle -> {
                val courtier = courtiers.find { it.name == action.recipient }
                if (courtier != null) {
                    courtier.loyalty = (courtier.loyalty + 15).coerceAtMost(100)
                    CourtResult.Success("Title granted to $action.recipient")
                } else {
                    CourtResult.Failure("Courtier not found")
                }
            }
            is CourtAction.DenyRequest -> {
                val courtier = courtiers.find { it.name == action.recipient }
                courtier?.let {
                    it.loyalty = (it.loyalty - 10).coerceAtLeast(0)
                }
                CourtResult.Success("Request denied")
            }
            is CourtAction.AppointToCouncil -> {
                val courtier = courtiers.find { it.name == action.recipient }
                if (courtier != null && councilMembers.size < 7) {
                    councilMembers.add(CouncilMember(courtier.name, action.position, courtier.effectiveness))
                    CourtResult.Success("${action.recipient} appointed to council")
                } else {
                    CourtResult.Failure("Cannot appoint to council")
                }
            }
        }
    }
    
    fun declareWar(target: String, casusBelli: String): WarResult {
        val existingWar = wars.find { it.enemy == target && !it.isEnded }
        if (existingWar != null) {
            return WarResult.AlreadyAtWar
        }
        
        val war = War(
            enemy = target,
            casusBelli = casusBelli,
            startedAt = System.currentTimeMillis(),
            battlesWon = 0,
            battlesLost = 0,
            territoryGained = 0
        )
        wars.add(war)
        
        // Mobilize army
        armySize += 5000
        morale = (morale + 10).coerceAtMost(100)
        
        return WarResult.Success(war)
    }
    
    fun proposeAlliance(target: String, terms: AllianceTerms): AllianceResult {
        val existingAlliance = alliances.find { it.partner == target }
        if (existingAlliance != null) {
            return AllianceResult.AlreadyAllied
        }
        
        // Check if target is at war with us
        val atWar = wars.any { it.enemy == target && !it.isEnded }
        if (atWar) {
            return AllianceResult.AtWar
        }
        
        val alliance = Alliance(
            partner = target,
            terms = terms,
            formedAt = System.currentTimeMillis(),
            trustLevel = 50
        )
        alliances.add(alliance)
        
        return AllianceResult.Success(alliance)
    }
    
    fun arrangeMarriage(partner: String, spouse: String): MarriageResult {
        // Check if already married
        // Check political benefits
        // Arrange the marriage
        
        val marriage = RoyalMarriage(
            spouse1 = spouse,
            spouse2 = partner,
            arrangedAt = System.currentTimeMillis(),
            politicalBenefit = 20
        )
        
        return MarriageResult.Success(marriage)
    }
    
    fun manageProvince(provinceId: String, action: ProvinceAction): ProvinceResult {
        val province = provinces.find { it.id == provinceId }
        if (province == null) {
            return ProvinceResult.Failure("Province not found")
        }
        
        return when (action) {
            is ProvinceAction.InvestInInfrastructure -> {
                if (treasury >= action.amount) {
                    treasury -= action.amount
                    province.development = (province.development + 10).coerceAtMost(100)
                    ProvinceResult.Success("Infrastructure improved")
                } else {
                    ProvinceResult.Failure("Insufficient funds")
                }
            }
            is ProvinceAction.RaiseTaxes -> {
                province.taxRate = (province.taxRate + 5).coerceAtMost(100)
                province.loyalty = (province.loyalty - 10).coerceAtLeast(0)
                ProvinceResult.Success("Taxes raised")
            }
            is ProvinceAction.SendAid -> {
                if (treasury >= action.amount) {
                    treasury -= action.amount
                    province.loyalty = (province.loyalty + 20).coerceAtMost(100)
                    ProvinceResult.Success("Aid sent")
                } else {
                    ProvinceResult.Failure("Insufficient funds")
                }
            }
        }
    }
    
    fun updateState(newState: RoyaltyState) {
        _royaltyState.value = newState
    }
    
    fun getCourtiers(): List<Courtier> = courtiers.toList()
    fun getCouncilMembers(): List<CouncilMember> = councilMembers.toList()
    fun getProvinces(): List<Province> = provinces.toList()
    fun getWars(): List<War> = wars.filter { !it.isEnded }
    fun getAlliances(): List<Alliance> = alliances.toList()
}

// Royalty State Data
data class RoyaltyState(
    val crownAuthority: Int = 50,
    val royalPrestige: Int = 50,
    val successionPosition: Int = 1,
    val heirId: Long? = null,
    val regencyActive: Boolean = false,
    val treasury: Double = 0.0,
    val armySize: Int = 0,
    val navySize: Int = 0,
    val morale: Int = 50,
    val provinceCount: Int = 0,
    val vassalCount: Int = 0
)

// Court Entities
data class Courtier(
    val name: String,
    val title: String,
    var loyalty: Int,
    val effectiveness: Int,
    val ambition: Int = 50,
    val rivalries: List<String> = emptyList(),
    val secrets: List<String> = emptyList()
)

data class CouncilMember(
    val name: String,
    val position: String,
    val effectiveness: Int,
    val tenure: Long = System.currentTimeMillis()
)

data class Province(
    val id: String,
    val name: String,
    var development: Int = 50,
    var loyalty: Int = 50,
    var taxRate: Int = 20,
    val population: Int = 10000,
    val resources: List<String> = emptyList()
) {
    val taxRevenue: Double get() = population * (taxRate / 100.0) * (development / 100.0)
}

// Diplomacy Entities
data class ForeignRelation(
    val kingdom: String,
    var opinion: Int = 0,
    val tradeValue: Double = 0.0,
    val isAtWar: Boolean = false
)

data class Alliance(
    val partner: String,
    val terms: AllianceTerms,
    val formedAt: Long,
    var trustLevel: Int,
    val expiresAt: Long? = null
)

data class AllianceTerms(
    val isDefensive: Boolean = true,
    val isOffensive: Boolean = false,
    val tradeAgreement: Boolean = true,
    val royalMarriage: Boolean = false,
    val tributeAmount: Double = 0.0
)

data class War(
    val enemy: String,
    val casusBelli: String,
    val startedAt: Long,
    var battlesWon: Int,
    var battlesLost: Int,
    var territoryGained: Int,
    val endedAt: Long? = null,
    val peaceTerms: String? = null
) {
    val isEnded: Boolean get() = endedAt != null
    val warScore: Int get() = ((battlesWon - battlesLost) * 10) + (territoryGained * 5)
}

data class RoyalMarriage(
    val spouse1: String,
    val spouse2: String,
    val arrangedAt: Long,
    val politicalBenefit: Int,
    val children: List<String> = emptyList()
)

// Actions and Results
sealed class CourtAction {
    data class GrantTitle(val recipient: String, val title: String) : CourtAction()
    data class DenyRequest(val recipient: String) : CourtAction()
    data class AppointToCouncil(val recipient: String, val position: String) : CourtAction()
    data class SendOnMission(val recipient: String, val mission: String) : CourtAction()
}

sealed class CourtResult {
    data class Success(val message: String) : CourtResult()
    data class Failure(val reason: String) : CourtResult()
}

sealed class ProvinceAction {
    data class InvestInInfrastructure(val amount: Double) : ProvinceAction()
    data class RaiseTaxes(val percentage: Int) : ProvinceAction()
    data class SendAid(val amount: Double) : ProvinceAction()
    data class StationTroops(val count: Int) : ProvinceAction()
}

sealed class ProvinceResult {
    data class Success(val message: String) : ProvinceResult()
    data class Failure(val reason: String) : ProvinceResult()
}

sealed class WarResult {
    data class Success(val war: War) : WarResult()
    object AlreadyAtWar : WarResult()
    object InsufficientForces : WarResult()
}

sealed class AllianceResult {
    data class Success(val alliance: Alliance) : AllianceResult()
    object AlreadyAllied : AllianceResult()
    object AtWar : AllianceResult()
    object Rejected : AllianceResult()
}

sealed class MarriageResult {
    data class Success(val marriage: RoyalMarriage) : MarriageResult()
    object AlreadyMarried : MarriageResult()
    object Rejected : MarriageResult()
}
