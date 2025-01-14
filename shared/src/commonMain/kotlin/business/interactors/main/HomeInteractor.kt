package business.interactors.main


import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.DataState
import business.core.NetworkState
import business.core.ProgressBarState
import business.datasource.network.main.MainService
import business.datasource.network.main.responses.toHome
import business.domain.main.Home
import business.util.createException
import business.util.handleUseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HomeInteractor(
    private val service: MainService,
    private val appDataStoreManager: AppDataStore,
 ) {

    val defaultHomeJson = "{\"result\":{\"address\":null,\"banners\":[{\"banner\":\"https://m.media-amazon.com/images/I/614VOsGXsqL._SX1500_.jpg\",\"id\":1},{\"banner\":\"https://m.media-amazon.com/images/I/71kFc7PP3PL._SX3000_.jpg\",\"id\":2},{\"banner\":\"https://m.media-amazon.com/images/I/717Qv6Rdi+L._SX3000_.jpg\",\"id\":3}],\"categories\":[{\"icon\":\"\",\"id\":1,\"name\":\"Computer\",\"parent\":0},{\"icon\":\"\",\"id\":2,\"name\":\"Electronics\",\"parent\":0},{\"icon\":\"\",\"id\":3,\"name\":\"Arts & Crafts\",\"parent\":0},{\"icon\":\"\",\"id\":4,\"name\":\"Automotive\",\"parent\":0},{\"icon\":\"\",\"id\":5,\"name\":\"Baby\",\"parent\":0},{\"icon\":\"\",\"id\":6,\"name\":\"Beauty and personal care\",\"parent\":0},{\"icon\":\"\",\"id\":7,\"name\":\"Women's Fashion\",\"parent\":0},{\"icon\":\"\",\"id\":8,\"name\":\"Men's Fashion\",\"parent\":0},{\"icon\":\"\",\"id\":9,\"name\":\"Health and Household\",\"parent\":0},{\"icon\":\"\",\"id\":10,\"name\":\"Home and Kitchen\",\"parent\":0},{\"icon\":\"\",\"id\":11,\"name\":\"Industrial and Scientific\",\"parent\":0},{\"icon\":\"\",\"id\":12,\"name\":\"Luggage\",\"parent\":0},{\"icon\":\"\",\"id\":13,\"name\":\"Movies & Television\",\"parent\":0},{\"icon\":\"\",\"id\":14,\"name\":\"Pet supplies\",\"parent\":0},{\"icon\":\"\",\"id\":15,\"name\":\"Sports and Outdoors\",\"parent\":0},{\"icon\":\"\",\"id\":16,\"name\":\"Tools & Home Improvement\",\"parent\":0},{\"icon\":\"\",\"id\":17,\"name\":\"Toys and Games\",\"parent\":0}],\"flash_sale\":{\"expired_at\":\"2025-01-16T08:42:21.494136Z\",\"products\":[{\"description\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"id\":1,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/712koZKUvHL._AC_SX679_.jpg\",\"isLike\":false,\"likes\":36,\"price\":19,\"rate\":3.5,\"title\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Amazfit Band 5 Activity Fitness Tracker with Alexa Built-in, 15-Day Battery Life, Blood Oxygen, Heart Rate, Sleep & Stress Monitoring, 5 ATM Water Resistant, Fitness Watch for Men Women Kids, Black\",\"id\":2,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51ja6ds+pML._AC_SX679_.jpg\",\"isLike\":false,\"likes\":21,\"price\":120,\"rate\":3.5,\"title\":\"Amazfit Band 5 Activity Fitness Tracker\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"id\":3,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/816gTVTqs5L._AC_SL1500_.jpg\",\"isLike\":false,\"likes\":7,\"price\":320,\"rate\":3.5,\"title\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":4,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":4,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":5,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":0,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":6,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":true,\"likes\":5,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Shirt model-131\",\"id\":7,\"image\":\"https://img.ltwebstatic.com/images3_pi/2022/08/22/1661139363d0af2b9ebc4be1a701c62b3af5e237ef.webp\",\"isLike\":false,\"likes\":10,\"price\":13,\"rate\":3.5,\"title\":\"Shirt model-131\",\"category\":null,\"comments\":null,\"gallery\":null}]},\"most_sale\":[{\"description\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"id\":1,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/712koZKUvHL._AC_SX679_.jpg\",\"isLike\":false,\"likes\":36,\"price\":19,\"rate\":3.5,\"title\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Amazfit Band 5 Activity Fitness Tracker with Alexa Built-in, 15-Day Battery Life, Blood Oxygen, Heart Rate, Sleep & Stress Monitoring, 5 ATM Water Resistant, Fitness Watch for Men Women Kids, Black\",\"id\":2,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51ja6ds+pML._AC_SX679_.jpg\",\"isLike\":false,\"likes\":21,\"price\":120,\"rate\":3.5,\"title\":\"Amazfit Band 5 Activity Fitness Tracker\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"id\":3,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/816gTVTqs5L._AC_SL1500_.jpg\",\"isLike\":false,\"likes\":7,\"price\":320,\"rate\":3.5,\"title\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":4,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":4,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":5,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":0,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":6,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":true,\"likes\":5,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Shirt model-131\",\"id\":7,\"image\":\"https://img.ltwebstatic.com/images3_pi/2022/08/22/1661139363d0af2b9ebc4be1a701c62b3af5e237ef.webp\",\"isLike\":false,\"likes\":10,\"price\":13,\"rate\":3.5,\"title\":\"Shirt model-131\",\"category\":null,\"comments\":null,\"gallery\":null}],\"newest_product\":[{\"description\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"id\":1,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/712koZKUvHL._AC_SX679_.jpg\",\"isLike\":false,\"likes\":36,\"price\":19,\"rate\":3.5,\"title\":\"MANGOPOP Women's Mock Turtle Neck Slim Fit Long Half Short Sleeve T Shirt Tight Tops Tee\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Amazfit Band 5 Activity Fitness Tracker with Alexa Built-in, 15-Day Battery Life, Blood Oxygen, Heart Rate, Sleep & Stress Monitoring, 5 ATM Water Resistant, Fitness Watch for Men Women Kids, Black\",\"id\":2,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/51ja6ds+pML._AC_SX679_.jpg\",\"isLike\":false,\"likes\":21,\"price\":120,\"rate\":3.5,\"title\":\"Amazfit Band 5 Activity Fitness Tracker\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"id\":3,\"image\":\"https://m.media-amazon.com/images/W/MEDIAX_792452-T1/images/I/816gTVTqs5L._AC_SL1500_.jpg\",\"isLike\":false,\"likes\":7,\"price\":320,\"rate\":3.5,\"title\":\"Alpine Corporation Weather-resistant Bluetooth Solar-Powered Outdoor Wireless Rock Speaker – Set of 2, Brown\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":4,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":4,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":5,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":false,\"likes\":0,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Nike model-934\",\"id\":6,\"image\":\"https://www.deadstock.de/wp-content/uploads/2023/01/Jordan-1-Mid-Barely-Grape-DQ8423-501-dead-stock-titel-bild-.jpeg\",\"isLike\":true,\"likes\":5,\"price\":120,\"rate\":3.5,\"title\":\"Nike model-934\",\"category\":null,\"comments\":null,\"gallery\":null},{\"description\":\"Shirt model-131\",\"id\":7,\"image\":\"https://img.ltwebstatic.com/images3_pi/2022/08/22/1661139363d0af2b9ebc4be1a701c62b3af5e237ef.webp\",\"isLike\":false,\"likes\":10,\"price\":13,\"rate\":3.5,\"title\":\"Shirt model-131\",\"category\":null,\"comments\":null,\"gallery\":null}]},\"status\":true,\"alert\":null}";

    fun execute(): Flow<DataState<Home>> = flow {

        try {

            emit(DataState.Loading(progressBarState = ProgressBarState.LoadingWithLogo))

            val token = appDataStoreManager.readValue(DataStoreKeys.TOKEN) ?: ""
            val apiResponse = service.home(token = token)

            // Todo - remove debugging
            val jsonString = Json.encodeToString(apiResponse)
            println(jsonString)

            if (apiResponse.status == false || apiResponse.result == null) {
                throw Exception(
                    apiResponse.alert?.createException()
                )
            }


            val result = apiResponse.result?.toHome()


            emit(DataState.NetworkStatus(NetworkState.Good))
            emit(DataState.Data(result))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.NetworkStatus(NetworkState.Failed))
            emit(handleUseCaseException(e))

        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }


    }


}