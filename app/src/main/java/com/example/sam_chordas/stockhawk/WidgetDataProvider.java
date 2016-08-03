package com.example.sam_chordas.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * WidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    Context mContext = null;
    Cursor cursor = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        long id = Binder.clearCallingIdentity();
        cursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID,
                            QuoteColumns.SYMBOL,
                            QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE,
                            QuoteColumns.CHANGE,
                            QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        Binder.restoreCallingIdentity(id);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        cursor.moveToPosition(position);
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.list_item_quote);

        view.setTextViewText(R.id.stock_symbol, cursor.getString(cursor.getColumnIndex("symbol")));
        view.setTextColor(R.id.stock_symbol, Color.BLACK);
        view.setTextViewText(R.id.bid_price, cursor.getString(cursor.getColumnIndex("bid_price")));
        view.setTextColor(R.id.bid_price, Color.BLACK);
        view.setTextViewText(R.id.change, cursor.getString(cursor.getColumnIndex("percent_change")));
        view.setInt(R.id.change, "setBackgroundColor", Color.parseColor("#00C853"));

        if (cursor.getString(cursor.getColumnIndex("change")).contains("-")) {
            view.setInt(R.id.change, "setBackgroundColor", Color.parseColor("#D50000"));
        } else {
            view.setInt(R.id.change, "setBackgroundColor", Color.parseColor("#00C853"));
        }
        return view;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
