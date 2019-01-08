package com.example.he.material.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.he.material.Interface.FilterListener;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter implements Filterable {
    //private List<String> list = new ArrayList<String>();
    private List<Songs> list =new ArrayList<Songs>();
    private Context context;
    private MyFilter filter = null;// 创建MyFilter对象
    private FilterListener listener = null;// 接口对象

   /* public MyAdapter(List<String> list, Context context, FilterListener filterListener) {
        this.list = list;
        this.context = context;
        this.listener = filterListener;
    }
*/
    public MyAdapter(List<Songs> list_Song, Context context, FilterListener filterListener) {
        this.list = list_Song;
        this.context = context;
        this.listener = filterListener;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //
            //convertView = LayoutInflater.from(context).inflate(R.layout.search_menu,null);
            convertView = LayoutInflater.from(context).inflate(R.layout.music_item,null);
            holder = new ViewHolder();
            holder.im_ss=(ImageView) convertView.findViewById(R.id.address_picture);
            holder.tv_ss = (TextView) convertView.findViewById(R.id.address_name);
            //holder.tv_ss = (TextView) convertView.findViewById(R.id.search_text);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Glide.with(context)
                .load(list.get(position).getPic())
                .into(holder.im_ss);
        holder.tv_ss.setText(list.get(position).getTitle());
        return convertView;
    }

    /**
     * 自定义MyAdapter类实现了Filterable接口，重写了该方法
     */
    @Override
    public Filter getFilter() {
        // 如果MyFilter对象为空，那么重写创建一个
        if (filter == null) {
            filter = new MyFilter(list);
        }
        return filter;
    }

    /**
     * 创建内部类MyFilter继承Filter类，并重写相关方法，实现数据的过滤
     *
     *
     */
    class MyFilter extends Filter {

        // 创建集合保存原始数据
        private List<Songs> original = new ArrayList<Songs>();

        public MyFilter(List<Songs> list) {
            this.original = list;
        }

        /**
         * 该方法返回搜索过滤后的数据
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 创建FilterResults对象
            FilterResults results = new FilterResults();

            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值给results
             *TextUtil是系统自带的一个对于字符串处理的工具类
             */
            if(TextUtils.isEmpty(constraint)){
                results.values = original;
                results.count = original.size();
            }else {
                // 创建集合保存过滤后的数据
                List<Songs> mList = new ArrayList<Songs>();
                // 遍历原始数据集合，根据搜索的规则过滤数据
                for(int i=0;i<original.size();i++){
                    // 这里就是过滤规则的具体实现【规则有很多，大家可以自己决定怎么实现】
                    String s =original.get(i).getTitle();
                    if(s.trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())){
                        //constraint.toString().trim().toLowerCase()====两组数据去掉两端空格，大写转为小写
                        //如果s字符串中包含有constraint中的数据，则匹配
                        // 规则匹配的话就往集合中添加该数据
                        mList.add(original.get(i));
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }

            // 返回FilterResults对象
            return results;
        }

        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 获取过滤后的数据
            list = (List<Songs>) results.values;
            // 如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            if(listener != null){
                listener.getFilterData(list);
            }
            // 刷新数据源显示
            notifyDataSetChanged();
        }

    }

    /**
     * 控件缓存类
     *
     *类似于recyclerview的viewholder
     *
     */

    class ViewHolder {
        ImageView im_ss;
        TextView tv_ss;
    }


}
