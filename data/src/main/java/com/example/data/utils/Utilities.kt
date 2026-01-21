package com.example.data.utils

import com.example.data.remote.model.ExpenseExtractionResult
import com.example.data.remote.model.GeminiContent
import com.example.data.remote.model.GeminiPart
import com.example.data.remote.model.PromptType
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getContentBasedOnPrompts(text: String, promptType: PromptType): List<GeminiContent> {
    return when (promptType) {
        PromptType.CATEGORY_SUGGESTION -> listOf(
            GeminiContent(
                parts = listOf(
                    GeminiPart("Suggest one spending category for : \"$text\" return only the category")
                )
            )
        )

        PromptType.FINAL_AMOUNT_EXTRACTION -> listOf(
            GeminiContent(
                parts = listOf(
                    GeminiPart("Extract the final payable amount from the following receipt text: --- $text --- return only the numeric value")
                )
            )
        )

        PromptType.EXTRACT_EXPENSE_DETAILS -> listOf(
            GeminiContent(
                parts = listOf(
                    GeminiPart(
                        """
                            Extract the following details from this receipt text: 
                                - Merchant or title
                                - Total amount (final payable)
                                - date (if available)
                                - Category of expense (suggest one, if not possible then "others")
                                - Try correcting the spelling of merchant string if possible
                                Return in JSON format like:
                                {"merchant":"...", "amount":"...", "date":"...", "category":"..."}
                                
                                receipt: $text
                            """.trimIndent()
                    )
                )
            )
        )
    }
}

fun normalizeCategory(category: String?): String {
    if (category.isNullOrBlank()) return "Other"
    
    val trimmed = category.trim()
    val key = trimmed.lowercase()

    // Map to standard buckets if they match keywords, otherwise keep the original name
    return when {
        foodKeywords.any { it == key || key.contains(it) } -> "Food & Drinks"
        travelKeywords.any { it == key || key.contains(it) } -> "Travel"
        shoppingKeywords.any { it == key || key.contains(it) } -> "Shopping"
        groceryKeywords.any { it == key || key.contains(it) } -> "Grocery"
        billsKeywords.any { it == key || key.contains(it) } -> "Bills"
        entertainmentKeywords.any { it == key || key.contains(it) } -> "Entertainment"
        else -> {
            // Capitalize first letter of each word for a professional look
            trimmed.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
        }
    }
}

val foodKeywords = listOf(
    "food",
    "restaurant",
    "cafe",
    "dining",
    "dinner",
    "lunch",
    "breakfast",
    "brunch",
    "meal",
    "snack",
    "coffee",
    "tea",
    "drink",
    "takeout",
    "delivery",
    "bakery",
    "pizza",
    "burger",
    "sushi",
    "pasta",
    "market",
    "supermarket",
    "eats",
    "kitchen",
    "cuisine",
    "diner",
    "bistro",
    "grill",
    "bar",
    "pub",
    "juice",
    "shake",
    "ice cream",
    "dessert",
    "cake",
    "sandwich",
    "salad",
    "chicken",
    "beef",
    "fish",
    "vegetarian",
    "vegan",
    "dhaba",
    "chaat",
    "tiffin",
    "kirana",
    "ration",
    "sabzi",
    "mandi",
    "zomato",
    "swiggy",
    "bhojanalya",
    "halwai"
)

val travelKeywords = listOf(
    "travel", "flight", "airline", "hotel", "motel", "booking", "airbnb",
    "vacation", "trip", "journey", "tour", "cruise", "rental car", "uber", "lyft",
    "taxi", "train", "bus", "gas", "fuel", "station", "resort", "lodge", "expedia",
    "kayak", "fare", "ticket"
)

val shoppingKeywords = listOf(
    "shopping",
    "apparel",
    "clothing",
    "shoes",
    "accessories",
    "boutique",
    "mall",
    "store",
    "shop",
    "outlet",
    "purchase",
    "order",
    "online shopping",
    "electronics",
    "fashion",
    "retail",
    "department store",
    "amazon",
    "ebay",
    "etsy",
    "flipkart",
    "myntra",
    "ajio",
    "nykaa",
    "meesho",
    "bazaar",
    "dukaan",
    "reliance digital",
    "croma",
    "big bazaar",
    "amazon"
)

val billsKeywords = listOf(
    "bill", "payment", "utility", "utilities", "electricity", "water", "gas",
    "internet", "cable", "phone", "mobile", "subscription", "rent", "mortgage",
    "insurance", "premium", "invoice", "fee", "charge", "tax", "loan",
    "bijli", "paani", "recharge", "postpaid", "prepaid", "broadband", "dth",
    "gpay", "paytm", "phonepe", "bhim", "upi", "emi", "challan"
)

val entertainmentKeywords = listOf(
    "entertainment",
    "movie",
    "cinema",
    "theater",
    "concert",
    "show",
    "tickets",
    "event",
    "game",
    "gaming",
    "sports",
    "bar",
    "pub",
    "club",
    "music",
    "streaming",
    "netflix",
    "spotify",
    "hulu",
    "disney+",
    "youtube",
    "playstation",
    "xbox",
    "nintendo",
    "museum",
    "park",
    "recreation",
    "pvr",
    "inox",
    "bookmyshow",
    "hotstar",
    "zee5",
    "sonyliv",
    "jiosaavn",
    "gaana",
    "ipl",
    "mela"
)
val groceryKeywords = listOf(
    "grocery", "supermarket", "market", "groceries", "produce", "dairy", "meat",
    "bakery", "pantry", "beverages", "snacks", "frozen foods", "canned goods",
    "walmart", "costco", "trader joe's", "whole foods", "target"
)

fun formatDateFromMillis(millis: Long?): String {
    if(millis == null) return "N/A"
    val instant = Instant.ofEpochMilli(millis)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

fun String.parseGeminiExtractExpenseJson(): ExpenseExtractionResult {
    return try {
        val jsonStart = this.indexOf("{")
        val jsonEnd = this.lastIndexOf("}")
        if (jsonStart == -1 || jsonEnd == -1) {
            return ExpenseExtractionResult()
        }
        val jsonString = this.substring(jsonStart, jsonEnd + 1)
        val json = JSONObject(jsonString)
        ExpenseExtractionResult(
            title = json.optString("merchant"),
            amount = json.optString("amount"),
            date = json.optString("date"),
            category = json.optString("category")
        )
    } catch (e: Exception) {
        e.printStackTrace()
        ExpenseExtractionResult()
    }
}
