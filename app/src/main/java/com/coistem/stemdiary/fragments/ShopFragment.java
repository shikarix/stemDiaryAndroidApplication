package com.coistem.stemdiary.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coistem.stemdiary.entities.GetUserInfo;
import com.coistem.stemdiary.OurData;
import com.coistem.stemdiary.R;
import com.coistem.stemdiary.SocketConnect;
import com.coistem.stemdiary.adapters.CartItemStatusAdapter;
import com.coistem.stemdiary.adapters.CartItemsAdapter;
import com.coistem.stemdiary.adapters.ChangeStatusAdapter;
import com.coistem.stemdiary.adapters.ShopItemsListAdapter;
import com.coistem.stemdiary.entities.AdminCartItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private boolean isCartEmpty = true;

    private FloatingActionButton adminStatusCart;
    private FloatingActionButton shoppingCartButton;

    private ChangeStatusAdapter changeStatusAdapter;

    private ArrayList<AdminCartItem> itemsList;

    private AlertDialog cartDialog;
    private AlertDialog pleaseWaitDialog;
    private AlertDialog adminCartDialog;
    private AlertDialog confirmDialog;

    private String basket = null;

    float[] hsv;
    int runColor;
    private boolean isConfirmedCartEmpty;

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
        balanceTxt.setText("Ваш баланс: "+ GetUserInfo.userCounterCoins+" коинов");
        shopLoading = view.findViewById(R.id.shopProgressBar);
        adminStatusCart = view.findViewById(R.id.adminShopButton);
        adminStatusCart.hide();
        shoppingCartButton = view.findViewById(R.id.shoppingcartButton);
        shoppingCartButton.hide();
        nothingInShopText = view.findViewById(R.id.nothingInShopText);
        confirmDialog = new AlertDialog.Builder(getContext())
                .setTitle("Подтвердите изменения")
                .setMessage("Вы изменили некоторые статусы. Хотите сохранить измения?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<AdminCartItem> changedList = changeStatusAdapter.getChangedList();
                                setItemsStatus(changedList);
                            }
                        }).start();

                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDialog.dismiss();
                        adminCartDialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        shoppingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartDialog();
            }
        });
        if (GetUserInfo.userAccessType.equals("ADMIN")) {
            adminStatusCart.show();
        }
        adminStatusCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminCartDialog();
            }
        });
        cartDialog = new AlertDialog.Builder(getContext()).create();
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
        pleaseWaitDialog = new AlertDialog.Builder(getContext())
                .setTitle("Производим операцию...")
                .setCancelable(false)
                .setView(R.layout.pleasewaitdialog)
                .create();
        return view;
    }

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> costs = new ArrayList<>();
    private ArrayList<Integer> itemIds = new ArrayList<>();
    private ArrayList<Integer> counts = new ArrayList<>();

    private ArrayList<String> cartItemNames = new ArrayList<>();
    private ArrayList<Integer> cartItemIds = new ArrayList<>();
    private ArrayList<String> cartItemCosts = new ArrayList<>();
    private ArrayList<String> cartImageUrls = new ArrayList<>();

    private ArrayList<String> inWorkItemNames = new ArrayList<>();
    private ArrayList<String> inWorkItemStatuses = new ArrayList<>();


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

    @Override
    public void onResume() {
        randomChangeColors();
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateAllShop();
            }
        }).start();
        super.onResume();
    }

    private void showAdminCartDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.changestatus_dialog, null);
        AlertDialog.Builder adminCartBuilder = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (changeStatusAdapter.getChangedList().size() != 0) {
                            confirmDialog.show();
                        }
                    }
                })
                .setTitle("Покупки пользователей:");
        RecyclerView changeStatusList = dialogView.findViewById(R.id.changestatus_list);
        changeStatusAdapter = new ChangeStatusAdapter(itemsList);
        changeStatusList.setAdapter(changeStatusAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        changeStatusList.setLayoutManager(layoutManager);
        adminCartDialog = adminCartBuilder.create();
        adminCartDialog.show();
    }

    private void updateAllShop() {
        isEmpty = true;
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cartDialog.dismiss();
                    shopLoading.setVisibility(View.VISIBLE);
                    nothingInShopText.setVisibility(View.INVISIBLE);
                    shopList.setAlpha(0);
                }
            });
        }
        takeItems("fsddsfkdsf");
        getUnconfBasket();
        getConfirmedBasket();
        if (GetUserInfo.userAccessType.equals("ADMIN")) {
            getAdminConfirmedBasket();
        }
        if (getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShopItemsListAdapter shopItemsListAdapter = new ShopItemsListAdapter(){

                        @Override
                        public void showToast(final String text) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void openPleaseWait() {
                            pleaseWaitDialog.show();
                        }

                        @Override
                        public void updateShop() {
                            updateAllShop();
                            pleaseWaitDialog.dismiss();
                        }
                    };

                    if(!isEmpty) {
                        shopList.setAlpha(1);
                        shopList.setAdapter(shopItemsListAdapter);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                        shopList.setLayoutManager(layoutManager);
                        shopLoading.setVisibility(View.INVISIBLE);
                        shoppingCartButton.show();
                    }
                }
            });
        }
    }

    private void getAdminConfirmedBasket() {
        SocketConnect socketConnect = new SocketConnect();
        try {
            basket = (String) socketConnect.execute(SocketConnect.GET_ADMIN_CONFIRMED_BASKET).get();
            if(!basket.equals(SocketConnect.CONNECTION_ERROR)) {
                if (basket.equals(SocketConnect.GO_DALEKO)) {

                } else {
                    String[] databases = basket.split("Андроид ");
                    System.out.println(basket);
                    basket = databases[1];
                    parseAdminBaskets(basket);
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

    private void parseAdminBaskets(String json) {
        itemsList = new ArrayList<>();
        try {
            itemsList.clear();
            JSONArray jsonArray = new JSONArray(json);
            ArrayList<AdminCartItem> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productName = jsonObject.getString("productName");
                String status = jsonObject.getString("status");
                Integer id = jsonObject.getInt("id");
                AdminCartItem adminCartItem = new AdminCartItem();
                adminCartItem.name = productName;
                adminCartItem.status = status;
                adminCartItem.id = id;
                list.add(adminCartItem);
            }
            itemsList = list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getConfirmedBasket() {
        isConfirmedCartEmpty = true;
        SocketConnect socketConnect = new SocketConnect();
        try {
            basket = (String) socketConnect.execute(SocketConnect.GET_CONFIRMED_BASKET).get();
            if(!basket.equals(SocketConnect.CONNECTION_ERROR)) {
                if (basket.equals(SocketConnect.GO_DALEKO)) {

                } else {
                    isConfirmedCartEmpty = false;
                    String[] databases = basket.split("Андроид ");
                    basket = databases[1];
                    parseConfBasket(basket);
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

    private void setItemsStatus(ArrayList<AdminCartItem> item) {
        SocketConnect socketConnect = new SocketConnect();
        boolean isSendOk = true;
        for (int i = 0; i < item.size(); i++) {
            AdminCartItem adminCartItem = item.get(i);
            String status = adminCartItem.status;
            Integer id = adminCartItem.id;
            try {
                String stat = (String) socketConnect.execute(SocketConnect.SEND_STATUS, status, id).get();
                if(!stat.equals(SocketConnect.CONNECTION_ERROR)) {
                    String[] databases = stat.split("Андроид ");
                    stat = databases[1];
                } else {
                    isSendOk = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(isSendOk) {
            if(getActivity() != null ) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Статусы успешно установлены!", Toast.LENGTH_SHORT).show();
                    }
                });
                updateAllShop();
            }
        } else {
            if(getActivity() != null ) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "При установке статусов произошла ошибка.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    
    private void parseConfBasket(String json) {
        inWorkItemNames.clear();
        inWorkItemStatuses.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String productName = jsonObject.getString("productName");
                //add image from server
                String status = jsonObject.getString("status");
                inWorkItemNames.add(productName);
                inWorkItemStatuses.add(status);
            }
            OurData.inWorkItemNames = new String[inWorkItemNames.size()];
            OurData.inWorkItemNames = inWorkItemNames.toArray(OurData.inWorkItemNames);
            OurData.inWorkItemStatuses = new String[inWorkItemStatuses.size()];
            OurData.inWorkItemStatuses = inWorkItemStatuses.toArray(OurData.inWorkItemStatuses);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUnconfBasket() {
        isCartEmpty = true;
        SocketConnect socketConnect = new SocketConnect();
        try {
            basket = (String) socketConnect.execute(SocketConnect.GET_UNCONFIRMED_BASKET).get();
            if(!basket.equals(SocketConnect.CONNECTION_ERROR)) {
                if (basket.equals(SocketConnect.GO_DALEKO)) {

                } else {
                    isCartEmpty = false;
                    String[] databases = basket.split("Андроид ");
                    basket = databases[1];
                    parseUnconfBasket(basket);
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
                int id = jsonObject.getInt("product");
                String itemCost = "error";
                for (int j = 0; j < OurData.itemIds.length; j++) {
                    if (OurData.itemIds[j] == id) {
                        System.out.println( OurData.itemCosts[j]);
                        itemCost = OurData.itemCosts[j];
                        imageUrl = OurData.itemImageUrls[j];
                        break;
                    }
                }

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
        try {
            String shop = (String)socketConnect.execute("shop", token).get();
            if(!shop.equals(SocketConnect.CONNECTION_ERROR)) {
                String[] databases = shop.split("Андроид ");
                shop = databases[1];
                System.out.println(shop);
                if (shop.equals("[]")) {
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
            } else {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    shopLoading.setVisibility(View.INVISIBLE);
                                    shopList.setAlpha(0);
                                    nothingInShopText.setVisibility(View.VISIBLE);
                                }
                            });
                            Toast.makeText(getContext(), "При получении данных возникла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void showCartDialog() {
        cartDialog.dismiss();
        pleaseWaitDialog.dismiss();
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
        TextView emptyCartText = dialogView.findViewById(R.id.emptyCartItems);
        TextView emptyAcceptedItems = dialogView.findViewById(R.id.emptyAcceptedItems);
        RecyclerView cartItemsList = dialogView.findViewById(R.id.cartItemsList);
        if(isCartEmpty) {
            emptyCartText.setVisibility(View.VISIBLE);
        } else {
            emptyCartText.setVisibility(View.INVISIBLE);
            CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(){
                @Override
                public void updateCart() {
                    updateAllShop();
                    pleaseWaitDialog.dismiss();
                    showCartDialog();
                }

                @Override
                public void showStatusToast(int purchaseType, boolean isSuccess) {
                    switch (purchaseType) {
                        case 0: {
                            if(isSuccess) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Покупка успешно подтверждена!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Произошла ошибка при подтверждении покупки", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;
                        }
                        case 1: {
                            if(isSuccess) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Покупка успешно отклонена!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Произошла ошибка при отклонении покупки", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void showPleaseWaitDialog() {
                    pleaseWaitDialog.show();
                }
            }; // prepare recycleview for cart items
            cartItemsList.setAdapter(cartItemsAdapter);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
            cartItemsList.setLayoutManager(lm);
        }
        if(isConfirmedCartEmpty) {
            emptyAcceptedItems.setVisibility(View.VISIBLE);
        } else {
            emptyAcceptedItems.setVisibility(View.INVISIBLE);
            RecyclerView inWorkItemsList = dialogView.findViewById(R.id.acceptedItemsList); // prepare recycleview for accepted items
            CartItemStatusAdapter cartItemStatusAdapter = new CartItemStatusAdapter();
            inWorkItemsList.setAdapter(cartItemStatusAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            inWorkItemsList.setLayoutManager(layoutManager);
        }

        cartDialog = cartItemsDialog.create();
        cartDialog.show();
    }

    private void parseItems(String jsonFile) {
        itemIds.clear();
        names.clear();
        imageURLs.clear();
        costs.clear();
        counts.clear();
        try {
            System.out.println(jsonFile);

            JSONArray items = new JSONArray(jsonFile);
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = items.getJSONObject(i);
                String name = jsonObject.getString("title");
                String img = jsonObject.getString("avatarUrl");
                Integer cost = jsonObject.getInt("cost");
                Integer itemId = jsonObject.getInt("id");
                Integer count = jsonObject.getInt("count");
                System.out.println(cost);
                System.out.println(name);
                System.out.println(img);
                itemIds.add(itemId);
                names.add(name);
                imageURLs.add(img);
                costs.add(cost.toString());
                counts.add(count);
            }

            OurData.itemNames = new String[names.size()];
            OurData.itemNames = names.toArray(OurData.itemNames);
            OurData.itemImageUrls = new String[imageURLs.size()];
            OurData.itemImageUrls = imageURLs.toArray(OurData.itemImageUrls);
            OurData.itemCosts = new String[costs.size()];
            OurData.itemCosts = costs.toArray(OurData.itemCosts);
            OurData.itemIds = new Integer[itemIds.size()];
            OurData.itemIds = itemIds.toArray(OurData.itemIds);
            OurData.itemCounts = new Integer[counts.size()];
            OurData.itemCounts = counts.toArray(OurData.itemCounts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        names.clear();
        imageURLs.clear();
        costs.clear();
        itemIds.clear();
        counts.clear();
        super.onPause();
    }



}