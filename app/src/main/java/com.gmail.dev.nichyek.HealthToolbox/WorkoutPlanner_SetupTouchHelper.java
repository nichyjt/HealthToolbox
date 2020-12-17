package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class WorkoutPlanner_SetupTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerView.Adapter mAdapter;
    ArrayList<WorkoutPlanner_Item> itemArrayList;
    Drawable trashIcon;
    ColorDrawable background = new ColorDrawable(Color.parseColor("#F44336"));

    public WorkoutPlanner_SetupTouchHelper(WorkoutPlanner_SetupAdapter adapter, ArrayList<WorkoutPlanner_Item> arrayList, Context context){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        itemArrayList = adapter.workoutPlannerItems;
        trashIcon = ContextCompat.getDrawable(context, R.drawable.ic_trash);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if(itemArrayList.size()==1){
            mAdapter.notifyItemChanged(pos);
            Snackbar.make(viewHolder.itemView, "You need at least one workout item!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        itemArrayList.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;

        int iconTop = itemView.getTop() + (itemView.getHeight() - trashIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + trashIcon.getIntrinsicHeight();
        int spicyMargin = 110;
        int backgroundCornerOffset = 0;

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + spicyMargin;
                int iconRight = iconLeft + trashIcon.getIntrinsicWidth();
                trashIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                background.setBounds(itemView.getLeft(), itemView.getTop()-backgroundCornerOffset,itemView.getLeft() + ((int) dX), itemView.getBottom()-backgroundCornerOffset);
            } else if (dX < 0) { // Swiping to the left
                int iconRight = itemView.getRight() - spicyMargin;// - iconMargin;
                int iconLeft = iconRight - trashIcon.getIntrinsicWidth();
                trashIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else {
                background.setBounds(0, 0, 0, 0);
            }
            background.draw(c);
            trashIcon.draw(c);
    }
}
