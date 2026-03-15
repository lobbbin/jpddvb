package com.popop.lifesimulator.domain.models

/**
 * Sealed class representing all faction types.
 */
sealed class FactionType(val value: String, val displayName: String, val icon: String = "🏛️") {
    
    // Royalty and Nobility
    object RoyalFamily : FactionType("royal_family", "Royal Family", "👑")
    object NobleHouse : FactionType("noble_house", "Noble House", "🏰")
    object Court : FactionType("court", "Royal Court", "🪑")
    
    // Political
    object PoliticalParty : FactionType("political_party", "Political Party", "🗳️")
    object Government : FactionType("government", "Government", "🏛️")
    object Senate : FactionType("senate", "Senate", "📜")
    object Council : FactionType("council", "Council", "👥")
    object Diplomats : FactionType("diplomats", "Diplomats", "🤝")
    
    // Criminal
    object CrimeFamily : FactionType("crime_family", "Crime Family", "🔫")
    object Gang : FactionType("gang", "Gang", "👊")
    object Syndicate : FactionType("syndicate", "Syndicate", "💼")
    object ThievesGuild : FactionType("thieves_guild", "Thieves Guild", "🗡️")
    object Smugglers : FactionType("smugglers", "Smugglers", "📦")
    object AssassinsGuild : FactionType("assassins_guild", "Assassins Guild", "🗡️")
    object Pirates : FactionType("pirates", "Pirates", "🏴‍☠️")
    
    // Business
    object Corporation : FactionType("corporation", "Corporation", "🏢")
    object TradeGuild : FactionType("trade_guild", "Trade Guild", "💰")
    object MerchantsGuild : FactionType("merchants_guild", "Merchants Guild", "🛒")
    object Bank : FactionType("bank", "Banking House", "🏦")
    object Investors : FactionType("investors", "Investors", "📈")
    object Union : FactionType("union", "Labor Union", "✊")
    
    // Military
    object Army : FactionType("army", "Army", "🎖️")
    object Navy : FactionType("navy", "Navy", "⚓")
    object Knights : FactionType("knights", "Knightly Order", "⚔️")
    object Mercenaries : FactionType("mercenaries", "Mercenaries", "💂")
    object Guard : FactionType("guard", "City Guard", "🛡️")
    
    // Religious
    object Church : FactionType("church", "Church", "⛪")
    object Cult : FactionType("cult", "Cult", "🔮")
    object Monastery : FactionType("monastery", "Monastery", "🙏")
    object ReligiousOrder : FactionType("religious_order", "Religious Order", "✝️")
    
    // Academic
    object University : FactionType("university", "University", "🎓")
    object ScholarsGuild : FactionType("scholars_guild", "Scholars Guild", "📚")
    object MagesGuild : FactionType("mages_guild", "Mages Guild", "🔮")
    object Researchers : FactionType("researchers", "Researchers", "🔬")
    
    // Social
    object SocialClub : FactionType("social_club", "Social Club", "🍷")
    object Charity : FactionType("charity", "Charity", "❤️")
    object Rebels : FactionType("rebels", "Rebels", "🔥")
    object Revolutionaries : FactionType("revolutionaries", "Revolutionaries", "✊")
    
    // Special
    object SecretSociety : FactionType("secret_society", "Secret Society", "👁️")
    object IntelligenceAgency : FactionType("intelligence", "Intelligence Agency", "🕵️")
    object Underground : FactionType("underground", "Underground Network", "🌑")
    object Generic : FactionType("generic", "Organization", "🏛️")
    
    companion object {
        fun fromValue(value: String): FactionType = when (value) {
            "royal_family" -> RoyalFamily
            "noble_house" -> NobleHouse
            "court" -> Court
            "political_party" -> PoliticalParty
            "government" -> Government
            "senate" -> Senate
            "council" -> Council
            "diplomats" -> Diplomats
            "crime_family" -> CrimeFamily
            "gang" -> Gang
            "syndicate" -> Syndicate
            "thieves_guild" -> ThievesGuild
            "smugglers" -> Smugglers
            "assassins_guild" -> AssassinsGuild
            "pirates" -> Pirates
            "corporation" -> Corporation
            "trade_guild" -> TradeGuild
            "merchants_guild" -> MerchantsGuild
            "bank" -> Bank
            "investors" -> Investors
            "union" -> Union
            "army" -> Army
            "navy" -> Navy
            "knights" -> Knights
            "mercenaries" -> Mercenaries
            "guard" -> Guard
            "church" -> Church
            "cult" -> Cult
            "monastery" -> Monastery
            "religious_order" -> ReligiousOrder
            "university" -> University
            "scholars_guild" -> ScholarsGuild
            "mages_guild" -> MagesGuild
            "researchers" -> Researchers
            "social_club" -> SocialClub
            "charity" -> Charity
            "rebels" -> Rebels
            "revolutionaries" -> Revolutionaries
            "secret_society" -> SecretSociety
            "intelligence" -> IntelligenceAgency
            "underground" -> Underground
            else -> Generic
        }
        
        fun getAllTypes(): List<FactionType> = listOf(
            RoyalFamily, NobleHouse, Court,
            PoliticalParty, Government, Senate, Council, Diplomats,
            CrimeFamily, Gang, Syndicate, ThievesGuild, Smugglers, AssassinsGuild, Pirates,
            Corporation, TradeGuild, MerchantsGuild, Bank, Investors, Union,
            Army, Navy, Knights, Mercenaries, Guard,
            Church, Cult, Monastery, ReligiousOrder,
            University, ScholarsGuild, MagesGuild, Researchers,
            SocialClub, Charity, Rebels, Revolutionaries,
            SecretSociety, IntelligenceAgency, Underground, Generic
        )
    }
}
