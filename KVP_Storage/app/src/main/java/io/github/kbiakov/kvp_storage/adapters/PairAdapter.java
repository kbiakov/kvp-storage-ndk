package io.github.kbiakov.kvp_storage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kbiakov.kvp_storage.R;
import io.github.kbiakov.kvp_storage.models.PairEntity;

public class PairAdapter extends ArrayAdapter<PairEntity> {

    private Context mContext;
    private ArrayList<PairEntity> mPairEntities;

    private static class ViewHolder {
        private TextView uiLabelKey;
        private TextView uiLabelValue;
        private TextView uiLabelType;
    }

    public PairAdapter(Context context, ArrayList<PairEntity> pairEntities) {
        super(context, R.layout.item_pair, pairEntities);
        this.mContext = context;
        this.mPairEntities = pairEntities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pair, null);

            vh = new ViewHolder();
            vh.uiLabelKey = (TextView) convertView.findViewById(R.id.uiLabelKey);
            vh.uiLabelValue = (TextView) convertView.findViewById(R.id.uiLabelValue);
            vh.uiLabelType = (TextView) convertView.findViewById(R.id.uiLabelType);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        PairEntity p = mPairEntities.get(position);
        vh.uiLabelKey.setText(p.getKey());
        vh.uiLabelValue.setText(p.getValue());
        vh.uiLabelType.setText(p.getType().toString());

        return convertView;
    }
}
