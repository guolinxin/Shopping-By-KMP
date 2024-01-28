package presentation.ui.main.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.core.UIComponentState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.component.CircleButton
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.FilterDialog
import presentation.component.SelectShippingDialog
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.component.noRippleClickable
import presentation.theme.BorderColor
import presentation.ui.main.checkout.view_model.CheckoutEvent
import presentation.ui.main.checkout.view_model.CheckoutState
import presentation.ui.main.checkout.view_model.shippingType
import presentation.ui.main.detail.BuyButtonBox
import presentation.ui.main.detail.view_model.DetailEvent
import presentation.ui.main.payment_method.view_model.PaymentMethodEvent


@Composable
fun CheckoutScreen(
    state: CheckoutState,
    events: (CheckoutEvent) -> Unit,
    navigateToAddress: () -> Unit,
    popup: () -> Unit
) {


    if (state.selectShippingDialogState == UIComponentState.Show) {
        SelectShippingDialog(state = state, events = events)
    }


    DefaultScreenUI(queue = state.errorQueue,
        onRemoveHeadFromQueue = { events(CheckoutEvent.OnRemoveHeadFromQueue) },
        progressBarState = state.progressBarState,
        networkState = state.networkState,
        onTryAgain = { events(CheckoutEvent.OnRetryNetwork) }) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp).align(Alignment.TopCenter)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircleButton(imageVector = Icons.Filled.ArrowBack, onClick = { popup() })
                    Text("Checkout", style = MaterialTheme.typography.titleLarge)
                    Spacer_8dp()
                }

                Spacer_32dp()

                Text("Shipping Address", style = MaterialTheme.typography.titleLarge)
                Spacer_8dp()
                ShippingBox(
                    title = "Home",
                    image = "location2.xml", detail = state.selectedAddress.getShippingAddress()
                ) {
                    navigateToAddress()
                }

                Spacer_16dp()
                Divider(color = BorderColor)
                Spacer_16dp()

                Text("Choose Shipping Type", style = MaterialTheme.typography.titleLarge)
                Spacer_8dp()
                ShippingBox(
                    title = state.selectedShipping.title,
                    image = "shipping.xml",
                    detail = state.selectedAddress.getShippingAddress(),
                ) {
                    events(CheckoutEvent.OnUpdateSelectShippingDialogState(UIComponentState.Show))
                }


            }

            Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                CheckoutButtonBox() {
                    popup()
                }
            }
        }

    }
}


@Composable
fun CheckoutButtonBox(onClick: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            DefaultButton(
                modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                text = "Submit"
            ) {
                onClick()
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ShippingBox(title: String, image: String, detail: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(image),
            null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer_4dp()
        Column(modifier = Modifier.fillMaxWidth(.7f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(detail, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer_4dp()
        Box(modifier = Modifier.wrapContentHeight(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier.border(
                    1.dp,
                    color = BorderColor,
                    MaterialTheme.shapes.medium
                ).noRippleClickable { onClick() }
            ) {
                Text(
                    "CHANGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
