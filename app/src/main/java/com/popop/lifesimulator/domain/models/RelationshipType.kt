package com.popop.lifesimulator.domain.models

/**
 * Sealed class representing all possible relationship types.
 * Provides type-safe relationship categorization.
 */
sealed class RelationshipType(val value: String, val displayName: String) {
    
    // Social relationships
    object Stranger : RelationshipType("stranger", "Stranger")
    object Acquaintance : RelationshipType("acquaintance", "Acquaintance")
    object Friend : RelationshipType("friend", "Friend")
    object CloseFriend : RelationshipType("close_friend", "Close Friend")
    object BestFriend : RelationshipType("best_friend", "Best Friend")
    
    // Family relationships
    object Parent : RelationshipType("parent", "Parent")
    object Child : RelationshipType("child", "Child")
    object Sibling : RelationshipType("sibling", "Sibling")
    object Grandparent : RelationshipType("grandparent", "Grandparent")
    object Grandchild : RelationshipType("grandchild", "Grandchild")
    object Uncle : RelationshipType("uncle", "Uncle")
    object Aunt : RelationshipType("aunt", "Aunt")
    object Cousin : RelationshipType("cousin", "Cousin")
    object Nephew : RelationshipType("nephew", "Nephew")
    object Niece : RelationshipType("niece", "Niece")
    
    // Romantic relationships
    object Crush : RelationshipType("crush", "Crush")
    object Dating : RelationshipType("dating", "Dating")
    object Engaged : RelationshipType("engaged", "Engaged")
    object Spouse : RelationshipType("spouse", "Spouse")
    object ExPartner : RelationshipType("ex_partner", "Ex-Partner")
    
    // Professional relationships
    object Colleague : RelationshipType("colleague", "Colleague")
    object Employee : RelationshipType("employee", "Employee")
    object Employer : RelationshipType("employer", "Employer")
    object BusinessPartner : RelationshipType("business_partner", "Business Partner")
    object Mentor : RelationshipType("mentor", "Mentor")
    object Student : RelationshipType("student", "Student")
    
    // Criminal relationships
    object Associate : RelationshipType("associate", "Associate")
    object Rival : RelationshipType("rival", "Rival")
    object Enemy : RelationshipType("enemy", "Enemy")
    object Informant : RelationshipType("informant", "Informant")
    object Handler : RelationshipType("handler", "Handler")
    object Victim : RelationshipType("victim", "Victim")
    object Accomplice : RelationshipType("accomplice", "Accomplice")
    
    // Political relationships
    object Ally : RelationshipType("ally", "Ally")
    object Vassal : RelationshipType("vassal", "Vassal")
    object Liege : RelationshipType("liege", "Liege")
    object Ruler : RelationshipType("ruler", "Ruler")
    object Subject : RelationshipType("subject", "Subject")
    object PoliticalRival : RelationshipType("political_rival", "Political Rival")
    
    // Special relationships
    object Nemesis : RelationshipType("nemesis", "Nemesis")
    object Benefactor : RelationshipType("benefactor", "Benefactor")
    object Protégé : RelationshipType("protege", "Protégé")
    object DebtHolder : RelationshipType("debt_holder", "Debt Holder")
    object SecretKeeper : RelationshipType("secret_keeper", "Secret Keeper")
    
    companion object {
        fun fromValue(value: String): RelationshipType = when (value) {
            "stranger" -> Stranger
            "acquaintance" -> Acquaintance
            "friend" -> Friend
            "close_friend" -> CloseFriend
            "best_friend" -> BestFriend
            "parent" -> Parent
            "child" -> Child
            "sibling" -> Sibling
            "grandparent" -> Grandparent
            "grandchild" -> Grandchild
            "uncle" -> Uncle
            "aunt" -> Aunt
            "cousin" -> Cousin
            "nephew" -> Nephew
            "niece" -> Niece
            "crush" -> Crush
            "dating" -> Dating
            "engaged" -> Engaged
            "spouse" -> Spouse
            "ex_partner" -> ExPartner
            "colleague" -> Colleague
            "employee" -> Employee
            "employer" -> Employer
            "business_partner" -> BusinessPartner
            "mentor" -> Mentor
            "student" -> Student
            "associate" -> Associate
            "rival" -> Rival
            "enemy" -> Enemy
            "informant" -> Informant
            "handler" -> Handler
            "victim" -> Victim
            "accomplice" -> Accomplice
            "ally" -> Ally
            "vassal" -> Vassal
            "liege" -> Liege
            "ruler" -> Ruler
            "subject" -> Subject
            "political_rival" -> PoliticalRival
            "nemesis" -> Nemesis
            "benefactor" -> Benefactor
            "protege" -> Protégé
            "debt_holder" -> DebtHolder
            "secret_keeper" -> SecretKeeper
            else -> Acquaintance
        }
        
        fun getAllTypes(): List<RelationshipType> = listOf(
            Stranger, Acquaintance, Friend, CloseFriend, BestFriend,
            Parent, Child, Sibling, Grandparent, Grandchild,
            Uncle, Aunt, Cousin, Nephew, Niece,
            Crush, Dating, Engaged, Spouse, ExPartner,
            Colleague, Employee, Employer, BusinessPartner, Mentor, Student,
            Associate, Rival, Enemy, Informant, Handler, Victim, Accomplice,
            Ally, Vassal, Liege, Ruler, Subject, PoliticalRival,
            Nemesis, Benefactor, Protégé, DebtHolder, SecretKeeper
        )
    }
}
