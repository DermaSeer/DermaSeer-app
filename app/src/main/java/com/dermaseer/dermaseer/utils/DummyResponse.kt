package com.dermaseer.dermaseer.utils

import com.dermaseer.dermaseer.data.remote.models.HistoryResponse
import com.dermaseer.dermaseer.data.remote.models.IngredientResponse
import com.dermaseer.dermaseer.data.remote.models.PredictResponse
import com.dermaseer.dermaseer.data.remote.models.ProductRecommendationResponse

fun getDummyPredictResponse(): PredictResponse {
   return PredictResponse(
      status = true,
      message = "Successfully predicted acne type",
      data = PredictResponse.Data(
         acneType = "Fulminans",
         imageUrl = "https://storage.googleapis.com/dermaseer/predict/M4JC_245DcYSinCkpDbbd.webp",
         predictId = "cm48tmall000ls60d1vilzwoi"
      )
   )
}

fun getDummyHistoryResponse(): HistoryResponse {
   return HistoryResponse(
      success = true,
      message = "Predictions successfully retrieved",
      data = listOf(
         HistoryResponse.Data(
            id = "xxxxx",
            acneType = "Fulminans",
            imageUrl = "https://storage.googleapis.com/dermaseer/predict/M4JC_245DcYSinCkpDbbd.webp",
            createdAt = "2024-12-03T19:13:39.876Z",
            result = HistoryResponse.Data.Result(
               id = "xxxxx",
               predictId = "cm48u7rqf000ts60dsgvetd6z",
               skinType = "Kering",
               productCategory = "Toner",
               ingredient = listOf(
                  "Glycolic Acid",
                  "Azelaic Acid",
                  "Salicylic Acid",
                  "Niacinamide",
                  "Centella Asiatica",
                  "Green Tea Extract",
                  "Witch Hazel",
                  "Rose Water",
                  "Chamomile Extract",
                  "Aloe Vera",
                  "Avocado Oil",
                  "Jojoba Oil",
                  "Rosehip Oil",
                  "Hyaluronic Acid",
                  "Panthenol",
                  "Licorice Root Extract",
                  "Bisabolol"
               ),
               msgRecommendation = "Untuk kulit kering dengan jerawat fulminans, gunakan toner dengan kandungan asam glikolat, asam salisilat atau asam azelaic. Gunakan juga toner dengan kandungan niacinamide karena bisa mengurangi minyak berlebih dan mencegah jerawat. Selain itu, tambahkan toner dengan bahan-bahan anti-inflamasi seperti centella asiatica, green tea extract, witch hazel atau licorice root extract. Hindari alkohol dan pewangi yang dapat mengiritasi kulit. Gunakan toner secara lembut dan rutin, serta jangan lupa saat menggunakan toner untuk menyeimbangkan dan menjaga kelembaban kulit.",
               createdAt = "2024-12-03T19:14:46.271Z",
               updatedAt = "2024-12-03T19:14:46.271Z"
            )
         ),
         HistoryResponse.Data(
            id = "zzzzz",
            acneType = "Papula",
            imageUrl = "https://storage.googleapis.com/dermaseer/predict/M4JC_245DcYSinCkpDbbd.webp",
            createdAt = "2024-12-05T19:13:39.876Z",
            result = HistoryResponse.Data.Result(
               id = "zzzzz",
               predictId = "cm48u7rqf000ts60dsgvetd6z",
               skinType = "Sensitif",
               productCategory = "Toner",
               ingredient = listOf(
                  "Glycolic Acid",
                  "Azelaic Acid",
                  "Salicylic Acid",
                  "Niacinamide",
                  "Centella Asiatica",
                  "Green Tea Extract",
                  "Witch Hazel",
                  "Rose Water",
                  "Chamomile Extract",
                  "Aloe Vera",
                  "Avocado Oil",
                  "Jojoba Oil",
                  "Rosehip Oil",
                  "Hyaluronic Acid",
                  "Panthenol",
                  "Licorice Root Extract",
                  "Bisabolol"
               ),
               msgRecommendation = "Untuk kulit kering dengan jerawat fulminans, gunakan toner dengan kandungan asam glikolat, asam salisilat atau asam azelaic. Gunakan juga toner dengan kandungan niacinamide karena bisa mengurangi minyak berlebih dan mencegah jerawat. Selain itu, tambahkan toner dengan bahan-bahan anti-inflamasi seperti centella asiatica, green tea extract, witch hazel atau licorice root extract. Hindari alkohol dan pewangi yang dapat mengiritasi kulit. Gunakan toner secara lembut dan rutin, serta jangan lupa saat menggunakan toner untuk menyeimbangkan dan menjaga kelembaban kulit.",
               createdAt = "2024-12-05T19:14:46.271Z",
               updatedAt = "2024-12-03T19:14:46.271Z"
            )
         )
      )
   )
}

fun getDummyIngredientResponse(): IngredientResponse {
   return IngredientResponse(
      status = true,
      message = "Successful get recommendation",
      data = IngredientResponse.Data(
         resultId = "cm48wgnb10007s60dlsi0epof",
         ingredient = listOf(
            "Glycolic Acid",
            "Salicylic Acid",
            "Niacinamide",
            "HA (Hyaluronic Acid)",
            "Aloe Vera",
            "Green Tea Extract",
            "Witch Hazel",
            "Chamomile Extract",
            "Rose Water",
            "Cucumber Extract",
            "Centella Asiatica Extract",
            "Bisabolol",
            "Panthenol",
            "Ceramides",
            "Sodium Hyaluronate",
            "Kojic Acid",
            "Azelaic Acid",
            "Tea Tree Oil"
         ),
         message = "Untuk kulit kering dan jerawat fulminans, pilih toner dengan kandungan asam AHA seperti glycolic acid atau salicylic acid, serta bahan pelembab seperti hyaluronic acid, aloe vera, dan ceramide. Untuk meredakan jerawat, gunakan toner dengan bahan anti-bakteri seperti tea tree oil,Witch Hazel, dan niacinamide. Gunakan toner secara rutin pagi dan malam untuk membantu menjaga kelembaban kulit dan mencerahkan wajah."
      )
   )
}

fun getDummyProductRecommendationResponse(): ProductRecommendationResponse {
   return ProductRecommendationResponse(
      success = true,
      message = "Product successfully retrieved",
      meta = ProductRecommendationResponse.Meta(
         page = 1,
         totalItems = 1,
         totalPage = 1
      ),
      data = listOf(
         ProductRecommendationResponse.Data(
            id = "cm48st8e6004ckwc259dzf3x9",
            name = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml (AGET 500ML)",
            category = "Perawatan & Kecantikan, Perawatan Wajah, Toner",
            shopName = "Autumn Official Store",
            imageUrl = "https://down-id.img.susercontent.com/file/id-11134207-7ras9-m0nuen2l6fq8a8@resize_w450_nl.webp",
            url = "https://shopee.co.id/product/31928548/26004554847",
            productRating = 4.872291440953413,
            price = 37900,
            description = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml\n\n\n7% Glycolic Acid (AHA) membantu mengeksfoliasi sel-sel kulit mati secara maksmial sehingga kulit kusam menjadi glowing dan bersih bersinar. \nNiacinamide yang membantu mencerahkan kulit, menyamarkan noda hitam dan menghindari kulit kusam.\nPanthenol (Vitamin B5) bekerja hingga menembus lapisan kulit dan mengunci kelembapan sehingga kulit selalu terhidrasi, serta membantu memperbaiki skin barrier yang rusak.\nCentella Asiatica, Mugwort & Tea Tree Extract mengandung anti inflamasi dan anti oxidant tinggi yang penting untuk merepair dan melindungi skin barrier.\nAloe Vera & Chamomile Extract banyak mengandung nutrisi yang dapat menjaga kelembapan kulit, mencegah iritasi dan kemerahan pada kulit, serta menyegarkan kulit.\n\nActive Ingredients\n•\t7% Glycolic Acid\n•\tNiacinamide\n•\tPanthenol\n•\tCentella Asiatica Extract\n•\tMugwort Extract\n•\tTea Tree Extract, Aloe Vera Extract, Chamomile Extract\n\nCara Pakai:\nGunakan pada malam hari, tidak lebih dari sekali per hari, setelah wajah dibersihkan. Pakailah pada bagian wajah dan leher yang tampak kusam dan berkomedo. Hindari area sekitar mata dan kulit yang luka serta kulit sensitif.\n\nPERINGATAN !!\n-\tSelama penggunaan hindari sinar matahari langsung. Gunakan tabir surya atau pelindung dari sinar matahari\n-\tJika terjadi reaksi hipersensitif (rasa terbakar, kemerahan atau tanda iritasi lain) pada kulit, hentikan pemakaian dan hubungi dokter.\n\n\n\n100% Produk Asli Indonesia\nPOM NA18231209227\n\nNetto: 500ml\n\nManufactured By :\nPT. Javinci Berkat Kreatif Sentosa\nCilegon - Indonesia\n\n",
            createdAt = "2024-12-03T18:34:20.838Z",
            updatedAt = "2024-12-03T18:34:20.838Z",
            matchCount = 6,
            matchedIngredients = listOf(
               "Glycolic Acid",
               "Niacinamide",
               "Centella Asiatica",
               "Panthenol",
               "Aloe Vera",
               "Chamomile Extract"
            )
         ),
         ProductRecommendationResponse.Data(
            id = "cm48st8e6004ckwc259dzf3x9",
            name = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml (AGET 500ML)",
            category = "Perawatan & Kecantikan, Perawatan Wajah, Toner",
            shopName = "Autumn Official Store",
            imageUrl = "https://down-id.img.susercontent.com/file/id-11134207-7ras9-m0nuen2l6fq8a8@resize_w450_nl.webp",
            url = "https://shopee.co.id/product/31928548/26004554847",
            productRating = 4.872291440953413,
            price = 37900,
            description = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml\n\n\n7% Glycolic Acid (AHA) membantu mengeksfoliasi sel-sel kulit mati secara maksmial sehingga kulit kusam menjadi glowing dan bersih bersinar. \nNiacinamide yang membantu mencerahkan kulit, menyamarkan noda hitam dan menghindari kulit kusam.\nPanthenol (Vitamin B5) bekerja hingga menembus lapisan kulit dan mengunci kelembapan sehingga kulit selalu terhidrasi, serta membantu memperbaiki skin barrier yang rusak.\nCentella Asiatica, Mugwort & Tea Tree Extract mengandung anti inflamasi dan anti oxidant tinggi yang penting untuk merepair dan melindungi skin barrier.\nAloe Vera & Chamomile Extract banyak mengandung nutrisi yang dapat menjaga kelembapan kulit, mencegah iritasi dan kemerahan pada kulit, serta menyegarkan kulit.\n\nActive Ingredients\n•\t7% Glycolic Acid\n•\tNiacinamide\n•\tPanthenol\n•\tCentella Asiatica Extract\n•\tMugwort Extract\n•\tTea Tree Extract, Aloe Vera Extract, Chamomile Extract\n\nCara Pakai:\nGunakan pada malam hari, tidak lebih dari sekali per hari, setelah wajah dibersihkan. Pakailah pada bagian wajah dan leher yang tampak kusam dan berkomedo. Hindari area sekitar mata dan kulit yang luka serta kulit sensitif.\n\nPERINGATAN !!\n-\tSelama penggunaan hindari sinar matahari langsung. Gunakan tabir surya atau pelindung dari sinar matahari\n-\tJika terjadi reaksi hipersensitif (rasa terbakar, kemerahan atau tanda iritasi lain) pada kulit, hentikan pemakaian dan hubungi dokter.\n\n\n\n100% Produk Asli Indonesia\nPOM NA18231209227\n\nNetto: 500ml\n\nManufactured By :\nPT. Javinci Berkat Kreatif Sentosa\nCilegon - Indonesia\n\n",
            createdAt = "2024-12-03T18:34:20.838Z",
            updatedAt = "2024-12-03T18:34:20.838Z",
            matchCount = 6,
            matchedIngredients = listOf(
               "Glycolic Acid",
               "Niacinamide",
               "Centella Asiatica",
               "Panthenol",
               "Aloe Vera",
               "Chamomile Extract"
            )
         ),
         ProductRecommendationResponse.Data(
            id = "cm48st8e6004ckwc259dzf3x9",
            name = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml (AGET 500ML)",
            category = "Perawatan & Kecantikan, Perawatan Wajah, Toner",
            shopName = "Autumn Official Store",
            imageUrl = "https://down-id.img.susercontent.com/file/id-11134207-7ras9-m0nuen2l6fq8a8@resize_w450_nl.webp",
            url = "https://shopee.co.id/product/31928548/26004554847",
            productRating = 4.872291440953413,
            price = 37900,
            description = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml\n\n\n7% Glycolic Acid (AHA) membantu mengeksfoliasi sel-sel kulit mati secara maksmial sehingga kulit kusam menjadi glowing dan bersih bersinar. \nNiacinamide yang membantu mencerahkan kulit, menyamarkan noda hitam dan menghindari kulit kusam.\nPanthenol (Vitamin B5) bekerja hingga menembus lapisan kulit dan mengunci kelembapan sehingga kulit selalu terhidrasi, serta membantu memperbaiki skin barrier yang rusak.\nCentella Asiatica, Mugwort & Tea Tree Extract mengandung anti inflamasi dan anti oxidant tinggi yang penting untuk merepair dan melindungi skin barrier.\nAloe Vera & Chamomile Extract banyak mengandung nutrisi yang dapat menjaga kelembapan kulit, mencegah iritasi dan kemerahan pada kulit, serta menyegarkan kulit.\n\nActive Ingredients\n•\t7% Glycolic Acid\n•\tNiacinamide\n•\tPanthenol\n•\tCentella Asiatica Extract\n•\tMugwort Extract\n•\tTea Tree Extract, Aloe Vera Extract, Chamomile Extract\n\nCara Pakai:\nGunakan pada malam hari, tidak lebih dari sekali per hari, setelah wajah dibersihkan. Pakailah pada bagian wajah dan leher yang tampak kusam dan berkomedo. Hindari area sekitar mata dan kulit yang luka serta kulit sensitif.\n\nPERINGATAN !!\n-\tSelama penggunaan hindari sinar matahari langsung. Gunakan tabir surya atau pelindung dari sinar matahari\n-\tJika terjadi reaksi hipersensitif (rasa terbakar, kemerahan atau tanda iritasi lain) pada kulit, hentikan pemakaian dan hubungi dokter.\n\n\n\n100% Produk Asli Indonesia\nPOM NA18231209227\n\nNetto: 500ml\n\nManufactured By :\nPT. Javinci Berkat Kreatif Sentosa\nCilegon - Indonesia\n\n",
            createdAt = "2024-12-03T18:34:20.838Z",
            updatedAt = "2024-12-03T18:34:20.838Z",
            matchCount = 6,
            matchedIngredients = listOf(
               "Glycolic Acid",
               "Niacinamide",
               "Centella Asiatica",
               "Panthenol",
               "Aloe Vera",
               "Chamomile Extract"
            )
         ),
         ProductRecommendationResponse.Data(
            id = "cm48st8e6004ckwc259dzf3x9",
            name = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml (AGET 500ML)",
            category = "Perawatan & Kecantikan, Perawatan Wajah, Toner",
            shopName = "Autumn Official Store",
            imageUrl = "https://down-id.img.susercontent.com/file/id-11134207-7ras9-m0nuen2l6fq8a8@resize_w450_nl.webp",
            url = "https://shopee.co.id/product/31928548/26004554847",
            productRating = 4.872291440953413,
            price = 37900,
            description = "Autumn Glycolic Acid 7% Toning Solution Exfoliating Toner (white) 500ml\n\n\n7% Glycolic Acid (AHA) membantu mengeksfoliasi sel-sel kulit mati secara maksmial sehingga kulit kusam menjadi glowing dan bersih bersinar. \nNiacinamide yang membantu mencerahkan kulit, menyamarkan noda hitam dan menghindari kulit kusam.\nPanthenol (Vitamin B5) bekerja hingga menembus lapisan kulit dan mengunci kelembapan sehingga kulit selalu terhidrasi, serta membantu memperbaiki skin barrier yang rusak.\nCentella Asiatica, Mugwort & Tea Tree Extract mengandung anti inflamasi dan anti oxidant tinggi yang penting untuk merepair dan melindungi skin barrier.\nAloe Vera & Chamomile Extract banyak mengandung nutrisi yang dapat menjaga kelembapan kulit, mencegah iritasi dan kemerahan pada kulit, serta menyegarkan kulit.\n\nActive Ingredients\n•\t7% Glycolic Acid\n•\tNiacinamide\n•\tPanthenol\n•\tCentella Asiatica Extract\n•\tMugwort Extract\n•\tTea Tree Extract, Aloe Vera Extract, Chamomile Extract\n\nCara Pakai:\nGunakan pada malam hari, tidak lebih dari sekali per hari, setelah wajah dibersihkan. Pakailah pada bagian wajah dan leher yang tampak kusam dan berkomedo. Hindari area sekitar mata dan kulit yang luka serta kulit sensitif.\n\nPERINGATAN !!\n-\tSelama penggunaan hindari sinar matahari langsung. Gunakan tabir surya atau pelindung dari sinar matahari\n-\tJika terjadi reaksi hipersensitif (rasa terbakar, kemerahan atau tanda iritasi lain) pada kulit, hentikan pemakaian dan hubungi dokter.\n\n\n\n100% Produk Asli Indonesia\nPOM NA18231209227\n\nNetto: 500ml\n\nManufactured By :\nPT. Javinci Berkat Kreatif Sentosa\nCilegon - Indonesia\n\n",
            createdAt = "2024-12-03T18:34:20.838Z",
            updatedAt = "2024-12-03T18:34:20.838Z",
            matchCount = 6,
            matchedIngredients = listOf(
               "Glycolic Acid",
               "Niacinamide",
               "Centella Asiatica",
               "Panthenol",
               "Aloe Vera",
               "Chamomile Extract"
            )
         )
      )
   )
}
