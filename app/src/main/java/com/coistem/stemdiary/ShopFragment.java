package com.coistem.stemdiary;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class ShopFragment extends Fragment {
    private RecyclerView shopList;

    private View view;
    public ImageView backgroundImg;
    private ImageView backgroundMaskImage;
    public static TextView balanceTxt;
    private ProgressBar shopLoading;
    private TextView nothingInShopText;

    private boolean isEmpty = true;

    private FloatingActionButton adminStatusCart;
    private FloatingActionButton shoppingCartButton;

    private AlertDialog cartDialog;

    private String basket = null;

    float[] hsv;
    int runColor;

//    private String jsonList = "[{\"title\":\"parashsa\",\"imgSrc\":\"something\",\"cost\":100}]";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        shopList = view.findViewById(R.id.shopList);
        backgroundImg = view.findViewById(R.id.backgroundShopImage);
        backgroundMaskImage = view.findViewById(R.id.backgroundMaskShopImage);
        balanceTxt = view.findViewById(R.id.balanceText);
        balanceTxt.setText("Ваш баланс: "+GetUserInfo.userCounterCoins+" коинов");
        shopLoading = view.findViewById(R.id.shopProgressBar);
        adminStatusCart = view.findViewById(R.id.adminShopButton);
        adminStatusCart.hide();
        shoppingCartButton = view.findViewById(R.id.shoppingcartButton);
        shoppingCartButton.hide();
        nothingInShopText = view.findViewById(R.id.nothingInShopText);
        shoppingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OurData.cartItemNames = new String[]{"Product 1", "Product 2", "Product 3", "Product 4"};
//                OurData.cartItemCosts = new String[]{"100", "200", "300", "400"};
//                OurData.cartItemImageUrls = new String[]{"something", "something", "something", "something"};
                OurData.inWorkItemNames = new String[]{"Product 1", "T-Shirt M", "Memory stick", "Notebook"};
                OurData.inWorkItemStatuses = new String[]{"Статус: Прочитано","Статус: Не прочитано", "Статус: Выполнено!", "Статус: Отказано"};
                showCartDialog();
            }
        });
//        Toast.makeText(getContext(), takeItems(GetUserInfo.userToken), Toast.LENGTH_SHORT).show();
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
//        valueAnimator.setDuration(10);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                System.out.println(animation.getAnimatedValue());
//                backgroundImg.setAlpha((Float)animation.getAnimatedValue());
//            }
//        });
//        changeColors();
//        ImageView mask = view.findViewById(R.id.backgroundMaskShopImage);
//        int rgb = Color.rgb(151, 101, 165);
//        mask.setBackgroundColor(Color.rgb(101, 51,115));
//        mask.setAlpha(0.3f);
//        backgroundImg.setBackgroundColor(rgb);
        return view;
    }

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> costs = new ArrayList<>();
    private ArrayList<Integer> itemIds = new ArrayList<>();

    private ArrayList<String> cartItemNames = new ArrayList<>();
    private ArrayList<Integer> cartItemIds = new ArrayList<>();
    private ArrayList<String> cartItemCosts = new ArrayList<>();
    private ArrayList<String> cartImageUrls = new ArrayList<>();




    public void randomChangeColors() {
        int[][] colors = new int[5][3];
        colors[0][0]=151;
        colors[0][1]=101;
        colors[0][2]=165;
        //-----
        colors[1][0]= 67;
        colors[1][1]=184;
        colors[1][2]=98;
        //-----
        colors[2][0]=245;
        colors[2][1]=88 ;
        colors[2][2]=125;
        //-----
        colors[3][0]= 63;
        colors[3][1]=81;
        colors[3][2]=181;
        //-----
        colors[4][0]=249;
        colors[4][1]=142;
        colors[4][2]=61;
        //-----
        Random rnd = new Random();
        int i = rnd.nextInt(colors.length);
        System.out.println("COUNT: "+i);
        int[] color = colors[i];
        int rgb = Color.rgb(color[0], color[1], color[2]);
        int maskRgb = Color.rgb(color[0]-50, color[1]-50, color[2]-50);
        backgroundMaskImage.setBackgroundColor(maskRgb);
        backgroundMaskImage.setAlpha(0.3f);
        backgroundImg.setBackgroundColor(rgb);
    }

    public void changeColors() {
        ValueAnimator anim = ValueAnimator.ofInt(0,360);
        anim.setDuration(2000);
        int hue = 0;
        hsv = new float[3]; // Transition color
        hsv[1] = 0.4f;
        hsv[2] = 1;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                hsv[0] = 360 * animation.getAnimatedFraction();
                int i = (Integer) animation.getAnimatedValue();
                System.out.println(i);
                hsv[0] = (float) i;
                runColor = Color.HSVToColor(hsv);
                backgroundImg.setBackgroundColor(runColor);
            }
        });
        anim.setRepeatCount(Animation.INFINITE);

        anim.start();
    }

    @Override
    public void onResume() {
        shopLoading.setVisibility(View.VISIBLE);
        nothingInShopText.setVisibility(View.INVISIBLE);
        randomChangeColors();
        new Thread(new Runnable() {
            @Override
            public void run() {
                takeItems("fsddsfkdsf");
                getUnconfBasket();
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShopItemsListAdapter shopItemsListAdapter = new ShopItemsListAdapter(){
                                @Override
                                public void makePurchase(int itemId, Context context) {
                                    super.makePurchase(itemId, context);
                                    getUnconfBasket();
                                }
                            };
                            if(!isEmpty) {
                                shopList.setAdapter(shopItemsListAdapter);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                                shopList.setLayoutManager(layoutManager);
                                shopLoading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        }).start();
        super.onResume();
    }

    private void getUnconfBasket() {
        SocketConnect socketConnect = new SocketConnect();
        try {
            basket = (String) socketConnect.execute(SocketConnect.GET_UNCONFIRMED_BASKET).get();
            if(!basket.equals(SocketConnect.GO_DALEKO) && !basket.equals(SocketConnect.CONNECTION_ERROR)) {
                String[] databases = basket.split("Андроид ");
                basket = databases[1];
                if (basket.equals("[]")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shoppingCartButton.hide();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shoppingCartButton.show();
                            parseUnconfBasket(basket);
                        }
                    });
                }
            } else {
                if (getActivity() !=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Возникла проблема при получении товаров из корзины.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void parseUnconfBasket(String json) {
        cartImageUrls.clear();
        cartItemNames.clear();
        cartItemIds.clear();
        cartItemCosts.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productName = jsonObject.getString("productName");
                //add image from server
                String imageUrl = "something";
                Integer basketId = jsonObject.getInt("id");
                Integer id = jsonObject.getInt("product");
                String itemCost = "error";
//                for (int j = 0; j < OurData.itemIds.length; j++) {
//                    if (OurData.itemIds[j].equals(id)) {
//                        itemCost = OurData.itemCosts[j];
//                        imageUrl = OurData.itemImageUrls[j];
//                    }
//                }

                cartItemNames.add(productName);
                cartImageUrls.add(imageUrl);
                cartItemIds.add(basketId);
                cartItemCosts.add(itemCost);
            }
            OurData.cartItemIds = new Integer[cartItemIds.size()];
            OurData.cartItemIds = cartItemIds.toArray(OurData.cartItemIds);
            OurData.cartItemCosts = new String[cartItemCosts.size()];
            OurData.cartItemCosts = cartItemCosts.toArray(OurData.cartItemCosts);
            OurData.cartItemNames = new String[cartItemNames.size()];
            OurData.cartItemNames = cartItemNames.toArray(OurData.cartItemNames);
            OurData.cartItemImageUrls = new String[cartImageUrls.size()];
            OurData.cartItemImageUrls = cartImageUrls.toArray(OurData.cartItemImageUrls);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void takeItems(String token) {
        SocketConnect socketConnect = new SocketConnect();
        String shop = "";
        try {
            shop = (String)socketConnect.execute("shop", token).get();
            String[] databases = shop.split("Андроид ");
            shop = databases[1];
            System.out.println(shop);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if(shop.equals("[]")) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shopLoading.setVisibility(View.INVISIBLE);
                    nothingInShopText.setVisibility(View.VISIBLE);
                }
            });
        } else {
            isEmpty = false;
            parseItems(shop);
        }
    }

    private void showCartDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.cart_dialog, null);
        AlertDialog.Builder cartItemsDialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cartDialog.dismiss();
                    }
                })
                .setTitle("Ваша корзина:");
        RecyclerView cartItemsList = dialogView.findViewById(R.id.cartItemsList);

        CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(); // prepare recycleview for cart items
        cartItemsList.setAdapter(cartItemsAdapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        cartItemsList.setLayoutManager(lm);

        RecyclerView inWorkItemsList = dialogView.findViewById(R.id.acceptedItemsList); // prepare recycleview for accepted items
        CartItemStatusAdapter cartItemStatusAdapter = new CartItemStatusAdapter();
        inWorkItemsList.setAdapter(cartItemStatusAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        inWorkItemsList.setLayoutManager(layoutManager);

        cartDialog = cartItemsDialog.create();
        cartDialog.show();
    }

    private void parseItems(String jsonFile) {
        try {
            System.out.println(jsonFile);

            JSONArray items = new JSONArray(jsonFile);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = items.getJSONObject(i);
                String name = jsonObject.getString("title");
                String img = jsonObject.getString("avatarUrl");
                Integer cost = jsonObject.getInt("cost");
                Integer itemId = jsonObject.getInt("id");
                System.out.println(cost);
                System.out.println(name);
                System.out.println(img);
                itemIds.add(itemId);
                names.add(name);
                imageURLs.add(img);
                costs.add(cost.toString());
            }

            OurData.itemNames = new String[names.size()];
            OurData.itemNames = names.toArray(OurData.itemNames);
            OurData.itemImageUrls = new String[imageURLs.size()];
            OurData.itemImageUrls = imageURLs.toArray(OurData.itemImageUrls);
            OurData.itemCosts = new String[costs.size()];
            OurData.itemCosts = costs.toArray(OurData.itemCosts);
            OurData.itemIds = new Integer[itemIds.size()];
            OurData.itemIds = itemIds.toArray(OurData.itemIds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        names.clear();
        imageURLs.clear();
        costs.clear();

        super.onPause();
    }
}
