package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class BuyActivity extends AppCompatActivity {

    private PurchasesUpdatedListener purchasesUpdatedListener;
    private BillingClient billingClient ;
    ProductDetails productDetails;

    private String inapp_type_1 = "free_image_animal_15_day";
    private String inapp_type_2 = "free_image_animal_1_day";
    private String inapp_type_3 = "free_image_animal_30_day";
    private String inapp_type_4 = "free_image_animal_3_day";
    private String inapp_type_5 = "free_image_animal_7_day";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


        //// tap 1
        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
             if (billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK && purchases !=null){
                 for (Purchase purchase:purchases){
                     if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()){
                         ConsumeParams consumeParams =
                                 ConsumeParams.newBuilder()
                                         .setPurchaseToken(purchase.getPurchaseToken())
                                         .build();

                         ConsumeResponseListener listener = new ConsumeResponseListener() {
                             @Override
                             public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                 if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                                 }
                             }
                         };

                         billingClient.consumeAsync(consumeParams, listener);
                     }
                 }
             }
            }
        };
        billingClient = BillingClient.newBuilder(BuyActivity.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();


        //// tap 2
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
                            .setProductList(ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(inapp_type_2)
                                            .setProductType(BillingClient.ProductType.INAPP)
                                            .build())).build();

                    billingClient.queryProductDetailsAsync(
                            queryProductDetailsParams, new ProductDetailsResponseListener() {
                                public void onProductDetailsResponse(BillingResult billingResult, List<ProductDetails> productDetailsList) {
                                    if (!productDetailsList.isEmpty()) {
                                        productDetails = productDetailsList.get(0);
                                        TextView nameBuy = (TextView) findViewById(R.id.name_buy);
                                        TextView  mony = (TextView) findViewById(R.id.mony);
                                        TextView  btnBuy1 = (TextView) findViewById(R.id.btn_buy1);
                                        nameBuy.setText(productDetails.getName());
                                        mony.setText((productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice()));

                                        btnBuy1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                                                        ImmutableList.of(
                                                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                                                        .setProductDetails(productDetails)
                                                                        .build()
                                                        );
                                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                        .setProductDetailsParamsList(productDetailsParamsList)
                                                        .build();

                                                billingClient.launchBillingFlow(BuyActivity.this, billingFlowParams);
                                            }
                                        });
                                    }


                                }
                            }
                    );
                }
            }
            @Override
            public void onBillingServiceDisconnected() {

            }
        });


    }
}