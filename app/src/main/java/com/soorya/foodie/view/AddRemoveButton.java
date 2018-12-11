package com.soorya.foodie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soorya.foodie.R;
import com.soorya.foodie.interfaces.AddRemoveButtonClickListener;
import com.soorya.foodie.interfaces.CartValueUpdater;

public class AddRemoveButton extends RelativeLayout {

    View rootView;
    TextView countTextView;
    View addButton;
    View removeButton;
    public AddRemoveButtonClickListener listener;

    private int minValue = 0;
    private int maxValue = 0;

    public AddRemoveButton(Context context) {
        super(context);
        init(context);
    }

    public AddRemoveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.add_remove_button, this);
        countTextView = (TextView) rootView.findViewById(R.id.item_count_text);

        addButton = rootView.findViewById(R.id.remove_item_button);
        removeButton = rootView.findViewById(R.id.add_item_button);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });
    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(countTextView.getText().toString());
        if(currentVal < maxValue) {
            currentVal++;
            countTextView.setText(String.valueOf(currentVal));

            if (listener!=null)
            {
                listener.onAddRemoveButtonClicked(currentVal);
            }
        }
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(countTextView.getText().toString());
        if(currentVal > minValue) {
            currentVal--;
            countTextView.setText(String.valueOf(currentVal));

            if (listener!=null)
            {
                listener.onAddRemoveButtonClicked(currentVal);
            }
        }
    }

    public int getItemCount()
    {
        try {
            return Integer.parseInt(countTextView.getText().toString());
        }
        catch (Exception e) {
            return 0;
        }
    }

    public void setItemCount(int count)
    {
        countTextView.setText(String.valueOf(count));
    }

    public void setMinValue(int val)
    {
        if (val >= Integer.MIN_VALUE)
            minValue = val;
    }

    public void setMaxValue(int val)
    {
        if (val <= Integer.MAX_VALUE)
            maxValue = val;
    }

    public void setOnAddRemoveButtonClickListener(AddRemoveButtonClickListener addRemoveButtonClickListener)
    {
        this.listener = addRemoveButtonClickListener;
    }
}