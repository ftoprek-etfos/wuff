package com.filiptoprek.wuff.presentation.rating

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.filiptoprek.wuff.R
import com.filiptoprek.wuff.domain.model.auth.Resource
import com.filiptoprek.wuff.domain.model.profile.UserProfile
import com.filiptoprek.wuff.domain.model.rating.Review
import com.filiptoprek.wuff.domain.model.reservation.Reservation
import com.filiptoprek.wuff.presentation.auth.AuthViewModel
import com.filiptoprek.wuff.presentation.reservation.ReservationViewModel
import com.filiptoprek.wuff.ui.theme.Opensans
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RateWalkerScreen(navController: NavHostController, ratingViewModel: RatingViewModel, authViewModel: AuthViewModel, reservation: Reservation, reservationViewModel: ReservationViewModel)
{
    var rating by remember {
        mutableStateOf(3)
    }

    var ratingFlow = ratingViewModel.ratingFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.Top),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Ocijenite šetača",
            style = TextStyle(
                fontFamily = Opensans,
                fontSize = 23.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            color = colorResource(R.color.gray)

        )
        Spacer(modifier = Modifier.size(20.dp))

        RatingBar(
            modifier = Modifier
                .size(50.dp),
            rating = rating.toDouble(),
            onRatingChanged = {
                rating = it.toInt()
            },
            starsColor = colorResource(R.color.green_accent)
        )
        Spacer(modifier = Modifier.size(20.dp))
        ratingFlow.value?.let {
            when(it){
                is Resource.Failure -> {
                }
                Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .wrapContentHeight(Alignment.CenterVertically),
                        color = colorResource(R.color.green_accent)
                    )
                }
                is Resource.Success -> {
                    navController.popBackStack()
                    reservationViewModel.refreshReservations()
                }
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
            .width(IntrinsicSize.Max),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green_accent)
            ),
            onClick = {
                ratingViewModel.addReview(Review(rating, authViewModel.currentUser?.uid.toString(), "${LocalDate.now().dayOfMonth}/${LocalDate.now().monthValue}/${LocalDate.now().year}", reservation.reservationId, reservation.walkerUserId))
            })
        {
            Text(
                modifier = Modifier,
                text = "Ocijeni",
                style = TextStyle(
                    fontFamily = Opensans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            )
        }
    }
}