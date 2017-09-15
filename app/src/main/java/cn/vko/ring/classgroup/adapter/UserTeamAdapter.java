package cn.vko.ring.classgroup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.classgroup.model.UserGroupTeamData;
import cn.vko.ring.common.base.BaseListAdapter;

/**
 * desc:
 * Created by jiarh on 16/5/18 18:19.
 */
public class UserTeamAdapter extends BaseListAdapter<UserGroupTeamData.DataEntity> {


    public UserTeamAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected ViewHolder createViewHolder(View root) {
        return new MyHodler(root);
    }

    @Override
    protected void fillView(View root, UserGroupTeamData.DataEntity item, ViewHolder holder, int position) {

        if (item==null)return;
        MyHodler h= (MyHodler) holder;
        h.teamName.setText(item.getTname());
    }

    @Override
    protected int getItemViewId() {
        return R.layout.user_team_item_view;
    }

    class MyHodler extends ViewHolder {

        public MyHodler(View view) {
            super(view);
        }
        @Bind(R.id.user_icon)
        ImageView userIcon;
        @Bind(R.id.team_name)
        TextView teamName;
    }
}
