package com.aronskiy_anton.sdk.managers;

import com.aronskiy_anton.sdk.Manager;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.library.CompleteErrorOnlyHandler;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.library.URLComposer;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.List;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class PayersCardsManager extends Manager {

    private Composer composer = new Composer();

    public PayersCardsManager(P2PCore core) {
        super(core);
    }

    class Composer extends URLComposer {

        String payers(){
            return relativeToApi("payers");
        }

        String payers(String id){
            return relative(payers(), id);
        }

        String payersCards(String id){
            return relative(payers(id), "cards");
        }

        String payersCardsCard(String id, int card){
            return relative(payersCards(id), String.valueOf(card));
        }
    }

    /**
     * Get all cards of payer
     * @return all cards of payer
     */
    public List<BankCard> cards(CompleteHandler<List<BankCard>, Throwable> callback){
        return core.networkManager.requestList(composer.payersCards(core.getPayerId()), NetworkManager.MethodType.GET, null, BankCard.class, callback);
    }

    /**
     * Get card of payer by id
     * @param cardId Id card
     * @return card of payer by id
     */
    public BankCard card(int cardId, CompleteHandler<BankCard, Throwable> callback){
        return core.networkManager.request(composer.payersCardsCard(core.getBenificaryId(), cardId), NetworkManager.MethodType.GET, null, BankCard.class, callback);
    }

    /**
     * Delete linked card of payer
     * @param cardId Id card
     */

    public void delete(int cardId, CompleteErrorOnlyHandler callback){
        core.networkManager.request(composer.payersCardsCard(core.getBenificaryId(), cardId), NetworkManager.MethodType.DELETE, null,  callback);
    }

}
