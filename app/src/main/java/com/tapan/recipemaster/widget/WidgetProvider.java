package com.tapan.recipemaster.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.tapan.recipemaster.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        directToWidgetService(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void directToWidgetService(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        Intent intent = new Intent(context,WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
        views.setRemoteAdapter(R.id.widget_stackView,intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}

