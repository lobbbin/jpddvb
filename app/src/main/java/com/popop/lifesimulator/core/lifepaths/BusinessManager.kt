package com.popop.lifesimulator.core.lifepaths

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Business Life Path Manager
 * Handles company management, investments, expansion, and tycoon mechanics
 */
class BusinessManager {
    
    private val _businessState = MutableStateFlow(BusinessState())
    val businessState: StateFlow<BusinessState> = _businessState.asStateFlow()
    
    // Company
    private var companyName: String = ""
    private var companyType: CompanyType = CompanyType.SOLE_PROPRIETORSHIP
    private var industry: Industry = Industry.TECH
    private var companySize: CompanySize = CompanySize.SOLO
    
    // Finance
    private var revenue: Double = 0.0
    private var expenses: Double = 0.0
    private var profit: Double = 0.0
    private var cashFlow: Double = 0.0
    private var valuation: Double = 0.0
    private var stockPrice: Double = 0.0
    
    // Operations
    private val employees = mutableListOf<Employee>()
    private val products = mutableListOf<Product>()
    private val locations = mutableListOf<BusinessLocation>()
    
    // Market
    private var marketShare: Float = 0f
    private val competitors = mutableListOf<Competitor>()
    private var brandRecognition: Int = 0
    
    // Investments
    private val investments = mutableListOf<Investment>()
    private val properties = mutableListOf<Property>()
    
    fun initialize(name: String, type: CompanyType, industry: Industry, startingCapital: Double) {
        companyName = name
        companyType = type
        this.industry = industry
        cashFlow = startingCapital
        valuation = startingCapital
    }
    
    // Product Management
    fun developProduct(name: String, category: ProductCategory, budget: Double): ProductResult {
        if (cashFlow < budget) {
            return ProductResult.InsufficientFunds
        }
        
        cashFlow -= budget
        
        val quality = (budget / 10000).toInt().coerceIn(1, 100)
        val product = Product(
            name = name,
            category = category,
            quality = quality,
            developmentCost = budget,
            price = budget * 0.2
        )
        
        products.add(product)
        return ProductResult.Success(product)
    }
    
    fun launchProduct(productId: String, marketingBudget: Double): LaunchResult {
        val product = products.find { it.id == productId } ?: return LaunchResult.NotFound
        
        if (cashFlow < marketingBudget) {
            return LaunchResult.InsufficientFunds
        }
        
        cashFlow -= marketingBudget
        
        val success = (product.quality + (marketingBudget / 1000)).toInt().coerceAtMost(100)
        val unitsSold = success * 100
        val revenue = unitsSold * product.price
        
        this.revenue += revenue
        marketShare += (success / 1000f)
        brandRecognition += success / 10
        
        return LaunchResult.Success(unitsSold, revenue)
    }
    
    // Employee Management
    fun hireEmployee(employee: Employee): HireResult {
        if (employees.size >= getMaxEmployees()) {
            return HireResult.CompanyFull
        }
        
        if (cashFlow < employee.salary) {
            return HireResult.InsufficientFunds
        }
        
        employees.add(employee)
        expenses += employee.salary
        companySize = calculateCompanySize()
        
        return HireResult.Success(employee)
    }
    
    fun fireEmployee(employeeId: String): FireResult {
        val employee = employees.find { it.id == employeeId } ?: return FireResult.NotFound
        
        if (employee.isKeyEmployee) {
            // Risk of negative consequences
            if (kotlin.random.Random.nextInt(100) < 30) {
                return FireResult.Lawsuit(employee.name)
            }
        }
        
        employees.removeAll { it.id == employeeId }
        expenses -= employee.salary
        companySize = calculateCompanySize()
        
        return FireResult.Success(employee.name)
    }
    
    fun promoteEmployee(employeeId: String): PromoteResult {
        val employee = employees.find { it.id == employeeId } ?: return PromoteResult.NotFound
        
        employee.salary *= 1.2
        employee.morale = (employee.morale + 20).coerceAtMost(100)
        employee.productivity = (employee.productivity + 10).coerceAtMost(100)
        
        return PromoteResult.Success(employee.name)
    }
    
    // Financial Management
    fun seekInvestment(investorType: InvestorType, equityOffered: Float): InvestmentResult {
        val valuation = this.valuation
        val investmentAmount = valuation * (equityOffered / 100f)
        
        val acceptanceChance = when (investorType) {
            InvestorType.ANGEL -> 60 + (equityOffered / 2).toInt()
            InvestorType.VC -> 40 + equityOffered.toInt()
            InvestorType.PE -> 30 + (equityOffered * 2).toInt()
            InvestorType.IPO -> if (companySize == CompanySize.ENTERPRISE) 70 else 20
        }.coerceAtMost(90)
        
        return if (kotlin.random.Random.nextInt(100) < acceptanceChance) {
            cashFlow += investmentAmount
            this.valuation += investmentAmount
            investments.add(Investment(investorType, investmentAmount, equityOffered))
            InvestmentResult.Success(investmentAmount, equityOffered, investorType)
        } else {
            InvestmentResult.Rejected
        }
    }
    
    fun applyForLoan(amount: Double, term: Int): LoanResult {
        val approvalChance = when {
            profit > 0 -> 80
            revenue > expenses -> 60
            else -> 30
        }
        
        return if (kotlin.random.Random.nextInt(100) < approvalChance) {
            val interestRate = 0.05 + (kotlin.random.Random.nextDouble() * 0.1)
            val monthlyPayment = (amount * (1 + interestRate)) / term
            
            cashFlow += amount
            expenses += monthlyPayment
            
            LoanResult.Approved(amount, interestRate, monthlyPayment)
        } else {
            LoanResult.Denied
        }
    }
    
    fun quarterlyReport(): FinancialReport {
        val grossProfit = revenue - expenses
        val netProfit = grossProfit * 0.7  // After taxes
        profit = netProfit
        cashFlow += netProfit
        
        return FinancialReport(
            revenue = revenue,
            expenses = expenses,
            grossProfit = grossProfit,
            netProfit = netProfit,
            cashFlow = cashFlow
        )
    }
    
    // Expansion
    fun openLocation(location: BusinessLocation): LocationResult {
        if (cashFlow < location.setupCost) {
            return LocationResult.InsufficientFunds
        }
        
        cashFlow -= location.setupCost
        expenses += location.monthlyCost
        
        locations.add(location)
        marketShare += 0.05f
        
        return LocationResult.Success(location)
    }
    
    fun acquireCompany(target: Competitor, offerAmount: Double): AcquisitionResult {
        if (cashFlow < offerAmount) {
            return AcquisitionResult.InsufficientFunds
        }
        
        val acceptanceChance = (offerAmount / target.valuation * 100).toInt().coerceAtMost(95)
        
        return if (kotlin.random.Random.nextInt(100) < acceptanceChance) {
            cashFlow -= offerAmount
            competitors.removeAll { it.id == target.id }
            marketShare += target.marketShare
            valuation += target.valuation * 0.8
            
            AcquisitionResult.Success(target.name, offerAmount)
        } else {
            AcquisitionResult.Rejected
        }
    }
    
    // Market Actions
    fun marketingCampaign(type: MarketingType, budget: Double): MarketingResult {
        if (cashFlow < budget) {
            return MarketingResult.InsufficientFunds
        }
        
        cashFlow -= budget
        
        val effectiveness = when (type) {
            MarketingType.DIGITAL -> budget / 5000
            MarketingType.TRADITIONAL -> budget / 10000
            MarketingType.INFLUENCER -> budget / 3000
            MarketingType.EVENT -> budget / 8000
        }.toInt().coerceAtMost(30)
        
        brandRecognition += effectiveness
        marketShare += (effectiveness / 1000f)
        
        return MarketingResult.Success(type, effectiveness)
    }
    
    fun priceWar(competitorId: String, priceCut: Float): PriceWarResult {
        val competitor = competitors.find { it.id == competitorId } ?: return PriceWarResult.NotFound
        
        if (priceCut < 0 || priceCut > 50) {
            return PriceWarResult.InvalidCut
        }
        
        // Simplified price war mechanics
        val ourStrength = (cashFlow / 10000).toInt() + brandRecognition
        val theirStrength = (competitor.valuation / 10000).toInt()
        
        return if (ourStrength >= theirStrength) {
            marketShare += 0.1f
            revenue -= (revenue * priceCut / 100)
            PriceWarResult.Victory(competitor.name)
        } else {
            marketShare -= 0.05f
            revenue -= (revenue * priceCut / 100)
            PriceWarResult.Defeat(competitor.name)
        }
    }
    
    // Exit Strategies
    fun sellCompany(buyer: String, offerAmount: Double): SaleResult {
        return if (offerAmount >= valuation * 0.8) {
            cashFlow += offerAmount
            SaleResult.Success(offerAmount, buyer)
        } else {
            SaleResult.Rejected(offerAmount)
        }
    }
    
    fun ipo(): IPOResult {
        if (companySize != CompanySize.ENTERPRISE) {
            return IPOResult.NotEligible
        }
        
        val sharesOffered = (valuation / 100).toInt()
        val pricePerShare = valuation / sharesOffered / 100
        
        stockPrice = pricePerShare
        cashFlow += valuation * 0.3  // Raised capital
        companyType = CompanyType.PUBLIC
        
        return IPOResult.Success(stockPrice, sharesOffered)
    }
    
    private fun getMaxEmployees(): Int = when (companySize) {
        CompanySize.SOLO -> 1
        CompanySize.SMALL -> 10
        CompanySize.MEDIUM -> 50
        CompanySize.LARGE -> 200
        CompanySize.ENTERPRISE -> Int.MAX_VALUE
    }
    
    private fun calculateCompanySize(): CompanySize = when {
        employees.size <= 1 -> CompanySize.SOLO
        employees.size <= 10 -> CompanySize.SMALL
        employees.size <= 50 -> CompanySize.MEDIUM
        employees.size <= 200 -> CompanySize.LARGE
        else -> CompanySize.ENTERPRISE
    }
    
    fun updateState(newState: BusinessState) {
        _businessState.value = newState.copy(
            companyName = companyName,
            revenue = revenue,
            expenses = expenses,
            profit = profit,
            valuation = valuation,
            marketShare = marketShare,
            employeeCount = employees.size,
            companySize = companySize
        )
    }
    
    fun getState(): BusinessState = _businessState.value
}

// Business State
data class BusinessState(
    val companyName: String = "",
    val companyType: CompanyType = CompanyType.SOLE_PROPRIETORSHIP,
    val industry: Industry = Industry.TECH,
    val companySize: CompanySize = CompanySize.SOLO,
    val revenue: Double = 0.0,
    val expenses: Double = 0.0,
    val profit: Double = 0.0,
    val valuation: Double = 0.0,
    val marketShare: Float = 0f,
    val employeeCount: Int = 0,
    val brandRecognition: Int = 0,
    val stockPrice: Double = 0.0,
    val isPublic: Boolean = false
)

enum class CompanyType {
    SOLE_PROPRIETORSHIP,
    PARTNERSHIP,
    LLC,
    CORPORATION,
    PUBLIC
}

enum class Industry {
    TECH,
    RETAIL,
    MANUFACTURING,
    FINANCE,
    HEALTHCARE,
    ENTERTAINMENT,
    REAL_ESTATE,
    ENERGY,
    FOOD,
    TRANSPORTATION
}

enum class CompanySize {
    SOLO,
    SMALL,
    MEDIUM,
    LARGE,
    ENTERPRISE
}

// Business Entities
data class Employee(
    val id: String,
    val name: String,
    val role: String,
    var salary: Double,
    var productivity: Int = 50,
    var morale: Int = 50,
    val skill: Int = 50,
    val isKeyEmployee: Boolean = false
)

data class Product(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val category: ProductCategory,
    var quality: Int,
    val developmentCost: Double,
    var price: Double,
    val unitsSold: Int = 0
)

enum class ProductCategory {
    SOFTWARE,
    HARDWARE,
    CONSUMER_GOODS,
    SERVICE,
    SUBSCRIPTION
}

data class BusinessLocation(
    val id: String,
    val name: String,
    val city: String,
    val setupCost: Double,
    val monthlyCost: Double,
    val revenuePotential: Double
)

data class Competitor(
    val id: String,
    val name: String,
    val valuation: Double,
    val marketShare: Float,
    val strength: Int
)

data class Investment(
    val investorType: InvestorType,
    val amount: Double,
    val equityPercentage: Float
)

enum class InvestorType {
    ANGEL,
    VC,
    PE,
    IPO
}

data class Property(
    val id: String,
    val type: PropertyType,
    val value: Double,
    val monthlyIncome: Double
)

enum class PropertyType {
    OFFICE,
    WAREHOUSE,
    RETAIL,
    LAND
}

// Results
sealed class ProductResult {
    data class Success(val product: Product) : ProductResult()
    object InsufficientFunds : ProductResult()
}

sealed class LaunchResult {
    data class Success(val unitsSold: Int, val revenue: Double) : LaunchResult()
    object NotFound : LaunchResult()
    object InsufficientFunds : LaunchResult()
}

sealed class HireResult {
    data class Success(val employee: Employee) : HireResult()
    object CompanyFull : HireResult()
    object InsufficientFunds : HireResult()
}

sealed class FireResult {
    data class Success(val employeeName: String) : FireResult()
    object NotFound : FireResult()
    data class Lawsuit(val employeeName: String) : FireResult()
}

sealed class PromoteResult {
    data class Success(val employeeName: String) : PromoteResult()
    object NotFound : PromoteResult()
}

sealed class InvestmentResult {
    data class Success(val amount: Double, val equity: Float, val investor: InvestorType) : InvestmentResult()
    object Rejected : InvestmentResult()
}

sealed class LoanResult {
    data class Approved(val amount: Double, val interestRate: Double, val monthlyPayment: Double) : LoanResult()
    object Denied : LoanResult()
}

data class FinancialReport(
    val revenue: Double,
    val expenses: Double,
    val grossProfit: Double,
    val netProfit: Double,
    val cashFlow: Double
)

sealed class LocationResult {
    data class Success(val location: BusinessLocation) : LocationResult()
    object InsufficientFunds : LocationResult()
}

sealed class AcquisitionResult {
    data class Success(val targetName: String, val amount: Double) : AcquisitionResult()
    object InsufficientFunds : AcquisitionResult()
    object Rejected : AcquisitionResult()
}

enum class MarketingType {
    DIGITAL,
    TRADITIONAL,
    INFLUENCER,
    EVENT
}

sealed class MarketingResult {
    data class Success(val type: MarketingType, val effectiveness: Int) : MarketingResult()
    object InsufficientFunds : MarketingResult()
}

sealed class PriceWarResult {
    data class Victory(val competitorName: String) : PriceWarResult()
    data class Defeat(val competitorName: String) : PriceWarResult()
    object NotFound : PriceWarResult()
    object InvalidCut : PriceWarResult()
}

sealed class SaleResult {
    data class Success(val amount: Double, val buyer: String) : SaleResult()
    data class Rejected(val amount: Double) : SaleResult()
}

sealed class IPOResult {
    data class Success(val pricePerShare: Double, val sharesOffered: Int) : IPOResult()
    object NotEligible : IPOResult()
}
