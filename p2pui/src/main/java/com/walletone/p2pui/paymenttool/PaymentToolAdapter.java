package com.walletone.p2pui.paymenttool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.walletone.p2pui.R;
import com.walletone.p2pui.library.CreditCardValidator.CreditCardValidator;
import com.walletone.p2pui.library.PaymentToolType;
import com.walletone.p2pui.loaders.DownloadImageTask;
import com.walletone.sdk.models.PaymentTool;

import java.util.List;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by aaronskiy on 21.09.2017.
 */

public class PaymentToolAdapter extends RecyclerView.Adapter<PaymentToolAdapter.ItemViewHolder> {

    public static final String IMAGE_URL_FOR_DOWNLOAD = "https://www.walletone.com/logo/paymenttype/%s.png?type=pt&w=50&h=50";

    private Context context;
    private CreditCardValidator validator;

    private List<PaymentTool> paymentTools;
    private PaymentToolFragment.PaymentToolItemListener itemListener;

    public PaymentToolAdapter(@NonNull List<PaymentTool> paymentTools, PaymentToolFragment.PaymentToolItemListener itemListener, Context context) {
        this.paymentTools = paymentTools;
        this.itemListener = itemListener;
        this.context = context;
        this.validator = new CreditCardValidator();
    }

    public void replaceData(List<PaymentTool> paymentTools) {
        setList(paymentTools);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<PaymentTool> paymentTools) {
        this.paymentTools = checkNotNull(paymentTools);
    }

    @Override
    public PaymentToolAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_tool_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(PaymentToolAdapter.ItemViewHolder holder, int position) {

        final PaymentTool paymentTool = paymentTools.get(position);

/*
        CreditCardType type = validator.type(paymentTool.getMask());
        if (type != CreditCardType.UNDEFINED) {
            Drawable drawable = ContextCompat.getDrawable(context, getCardTypeLogo(type.getTitle()));
            holder.image.setImageDrawable(drawable);
            holder.title.setText(type.getTitle());
        } else {
            holder.title.setText(R.string.unknown_card_type);
        }

        holder.number.setText(formatMask(paymentTool.getMask()));
*/
        PaymentToolType toolType = PaymentToolType.getPaymentToolNameByPaymentTypeId(paymentTool.getPaymentTypeId());
        holder.title.setText(context.getString(toolType.getLocalizedName()));

        String imageUrl = String.format(Locale.US, IMAGE_URL_FOR_DOWNLOAD, paymentTool.getPaymentTypeId());
        new DownloadImageTask(holder.image).execute(imageUrl);

        holder.number.setText(paymentTool.getMask());
        holder.setPaymentTool(paymentTool);
        holder.setOnPaymentToolItemClickListener(itemListener);

    }

    @Override
    public int getItemCount() {
        return paymentTools.size();
    }

    public void removeItem(int position) {
        paymentTools.remove(position);
        notifyItemRemoved(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title;
        TextView number;
        PaymentToolFragment.PaymentToolItemListener itemListener;
        PaymentTool paymentTool;

        ItemViewHolder(View v) {
            super(v);
            this.image = v.findViewById(R.id.payment_tool_logo);
            this.title = v.findViewById(R.id.payment_tool_title);
            this.number = v.findViewById(R.id.card_number);
            v.setOnClickListener(this);
        }

        public void setPaymentTool(PaymentTool paymentTool) {
            this.paymentTool = paymentTool;
        }

        public PaymentTool getPaymentTool() {
            return paymentTool;
        }

        public void setOnPaymentToolItemClickListener(PaymentToolFragment.PaymentToolItemListener itemListener) {
            this.itemListener = itemListener;
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null) {
                itemListener.onPaymentToolClick(paymentTool);
            }
        }
    }
/*
    private int getCardTypeLogo(String name){
        if("MasterCard".equals(name)){
            return R.drawable.ic_mastercard_mini;
        } else if("Visa".equals(name)){
            return R.drawable.ic_visa_mini;
        } else if ("Mir".equals(name)){
            return R.drawable.ic_mir_mini;
        } else return -1;
    }

    private String formatMask(String number) {
        if (number.length() <= 4) {
            return number;
        }

        String last4Digits = number.substring(number.length() - 4);
        last4Digits = last4Digits.replace('*', '•');
        return String.format("•••• %s", last4Digits);
    }
*/
}
