package com.popop.lifesimulator.data.database

import androidx.room.TypeConverter
import com.popop.lifesimulator.data.models.character.Gender
import com.popop.lifesimulator.data.models.character.LifePath
import com.popop.lifesimulator.data.models.character.Skill
import com.popop.lifesimulator.data.models.character.SkillRegistry
import com.popop.lifesimulator.data.models.character.SkillType
import com.popop.lifesimulator.data.models.relationship.RelationshipType
import com.popop.lifesimulator.data.models.world.FactionCategory
import com.popop.lifesimulator.data.models.world.LocationType
import org.json.JSONArray
import org.json.JSONObject

class Converters {
    
    // Gender converters
    @TypeConverter
    fun fromGender(gender: Gender): String = gender.name
    
    @TypeConverter
    fun toGender(value: String): Gender = Gender.valueOf(value)
    
    // LifePath converters
    @TypeConverter
    fun fromLifePath(lifePath: LifePath?): String? = lifePath?.name
    
    @TypeConverter
    fun toLifePath(value: String?): LifePath? = value?.let { LifePath.valueOf(it) }
    
    // LocationType converters
    @TypeConverter
    fun fromLocationType(type: LocationType): String = type.name
    
    @TypeConverter
    fun toLocationType(value: String): LocationType = LocationType.valueOf(value)
    
    // FactionCategory converters
    @TypeConverter
    fun fromFactionCategory(category: FactionCategory): String = category.name
    
    @TypeConverter
    fun toFactionCategory(value: String): FactionCategory = FactionCategory.valueOf(value)
    
    // RelationshipType converters
    @TypeConverter
    fun fromRelationshipType(type: RelationshipType): String = type.name
    
    @TypeConverter
    fun toRelationshipType(value: String): RelationshipType = RelationshipType.valueOf(value)
    
    // SkillRegistry converters
    @TypeConverter
    fun fromSkillRegistry(registry: SkillRegistry): String {
        val json = JSONObject()
        registry.skills.forEach { (skillType, skill) ->
            val skillJson = JSONObject().apply {
                put("type", skillType.name)
                put("level", skill.level)
                put("experience", skill.experience)
                put("lastUsed", skill.lastUsed)
            }
            json.put(skillType.name, skillJson)
        }
        return json.toString()
    }
    
    @TypeConverter
    fun toSkillRegistry(value: String): SkillRegistry {
        val skills = mutableMapOf<SkillType, Skill>()
        try {
            val json = JSONObject(value)
            json.keys().forEach { key ->
                val skillJson = json.getJSONObject(key)
                val skillType = SkillType.valueOf(key)
                val skill = Skill(
                    type = skillType,
                    level = skillJson.getInt("level"),
                    experience = skillJson.getDouble("experience"),
                    lastUsed = skillJson.getLong("lastUsed")
                )
                skills[skillType] = skill
            }
        } catch (e: Exception) {
            // Return empty registry on parse error
        }
        return SkillRegistry(skills)
    }
    
    // List<String> converters for traits
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        val jsonArray = JSONArray()
        list.forEach { jsonArray.put(it) }
        return jsonArray.toString()
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val list = mutableListOf<String>()
        try {
            val jsonArray = JSONArray(value)
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
        } catch (e: Exception) {
            // Return empty list on parse error
        }
        return list
    }
    
    // Map<String, Int> converters
    @TypeConverter
    fun fromIntMap(map: Map<String, Int>): String {
        val json = JSONObject()
        map.forEach { (key, value) ->
            json.put(key, value)
        }
        return json.toString()
    }
    
    @TypeConverter
    fun toIntMap(value: String): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        try {
            val json = JSONObject(value)
            json.keys().forEach { key ->
                map[key] = json.getInt(key)
            }
        } catch (e: Exception) {
            // Return empty map on parse error
        }
        return map
    }
}
