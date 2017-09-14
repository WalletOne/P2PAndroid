package com.aronskiy_anton.sdk.managers;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.aronskiy_anton.sdk.Manager;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.library.Base64;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.library.URLComposer;
import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class BeneficiariesCardsManager extends Manager {

    private Composer composer = new Composer();

    public BeneficiariesCardsManager(P2PCore core) {
        super(core);
    }

    /**
     * Get all cards of beneficiary
     * @return all cards of beneficiary
     */
    public List<BankCard> cards(CompleteHandler<List<BankCard>, Throwable> callback){
        return core.networkManager.requestList(composer.beneficiariesCards(core.getBenificaryId()), NetworkManager.MethodType.GET, null, BankCard.class, callback);
    }

    /**
     * Get card of beneficiary by id
     * @param cardId Id card
     * @return card of beneficiary by id
     */
    public BankCard card(int cardId, CompleteHandler<BankCard, Throwable> callback){
        return core.networkManager.request(composer.beneficiariesCardsCard(core.getBenificaryId(), cardId), NetworkManager.MethodType.GET, null, BankCard.class, callback);
    }

    /**
     * Delete linked card of beneficiary
     * @param cardId Id card
     */

    public void delete(int cardId){
        core.networkManager.request(composer.beneficiariesCardsCard(core.getBenificaryId(), cardId), NetworkManager.MethodType.DELETE, null, null, null);
    }

    /**
     * Link new bank card request
     * @param returnUrl Url to back user redirect
     * @return request to API
     */
    public RequestBuilder linkNewCardRequest(String returnUrl){

        final String urlString = "https://api.dev.walletone.com/p2p/beneficiary/card";
        final String timestamp = NetworkManager.ISO8601TimeStamp.getISO8601TimeStamp(new Date());

        Map<String, String> items = new TreeMap<>();
        items.put("PhoneNumber", core.getBenificaryPhoneNumber());
        items.put("PlatformBeneficiaryId", core.getBenificaryId());
        items.put("PlatformId", core.getPlatformId());
        items.put("RedirectToCardAddition", "true");
        items.put("ReturnUrl", returnUrl);
        items.put("Timestamp", timestamp);
        items.put("Title", core.getBenificaryTitle());

        final String signature = core.networkManager.makeSignatureForWeb(items);
        items.put("Signature", signature);

        ArrayList<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry: items.entrySet()) {
            params.add(String.format("%s=%s", entry.getKey(), Uri.encode(entry.getValue(), "UTF-8")));
        }

        final  String queryString = TextUtils.join("&", params);
        System.out.print(queryString);
        Log.d("REQUESTS", queryString);

        RequestBuilder.Builder builder = RequestBuilder.newBuilder()
                .setMethodType(NetworkManager.MethodType.POST)
                .setSignature(signature)
                .setTimestamp(timestamp)
                .setUrlString(urlString)
                .setHttpBody(queryString);

        return builder.build();
    }

    class Composer extends URLComposer{
        String beneficiary(){
            return "beneficiary";
        }

        String beneficiaryCard(){
            return relative(beneficiary(), "card");
        }

        String beneficiaries(){
            return relativeToApi("beneficiaries");
        }

        String beneficiaries(String id){
            return relative(beneficiaries(), id);
        }

        String beneficiariesCards(String id){
            return relative(beneficiaries(id), "cards");
        }

        String beneficiariesCardsCard(String id, int card){
            return relative(beneficiariesCards(id), String.valueOf(card));
        }
    }
}
