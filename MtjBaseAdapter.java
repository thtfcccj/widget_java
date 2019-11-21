package com.cofdet.dap8600.widget_java;
//多列时ListView管理
//出自：https://blog.csdn.net/qq_28779083/article/details/61419297

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MtjBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> listDatas = null;
    protected int mLayoutId;
    protected int column = 1;// 每行要显示的列数[默认是1]
    protected int line_int;// 计算得到的行数
    protected int column_yu;// 一行多列，不能整除时，最后一行的列数

    /**
     * 适配器
     *
     * @param context
     *            上下文
     * @param data
     *            数据源
     * @param layoutId
     *            layout资源文件ID
     * @param setcolumn
     *            设置每行要显示的列数[默认是1]
     */
    public MtjBaseAdapter(Context context, List<T> data, int layoutId,
                          int setcolumn) {
        this.mContext = context;
        this.listDatas = data;
        this.mLayoutId = layoutId;
        if (setcolumn >= 1) {
            column = setcolumn;
        }
    }

    @Override
    public int getCount() {
        column_yu = listDatas.size() % column;
        if (column_yu > 0) {
            line_int = listDatas.size() / column + 1;
        } else {
            line_int = listDatas.size() / column;
        }
        return line_int;
    }

    @Override
    public T getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 添加单条数据项
     *
     * @param item
     */
    public void addItem(T item) {
        this.listDatas.add(item);
    }

    /**
     * 设置数据源
     *
     * @param data
     */
    public void setListDatas(List<T> data) {
        this.listDatas = data;
    }

    /**
     * 清除数据源
     */
    public void clear() {
        this.listDatas.clear();
    }

    /**
     * 刷新数据源
     */
    public void refresh() {
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MtjBaseViewHolder holder = MtjBaseViewHolder.get(mContext, convertView,
                parent, mLayoutId);
        List<T> models = new ArrayList<T>();
        int[] positions = null;
        //可以被整除，正常返回每行的数据
        if (column_yu == 0) {
            positions = new int[column];
            for (int i = 0; i < column; i++) {
                int posi = position * column + i;
                T model = listDatas.get(posi);
                models.add(model);
                positions[i] = posi;
            }
        } else {
            //不能整除时，判断
            // 是否是最后一行，是，返回剩余的列的数据
            if (position == listDatas.size() / column) {
                positions = new int[column_yu];
                for (int i = 0; i < column_yu; i++) {
                    int posi = position * column + i;
                    T model = listDatas.get(posi);
                    models.add(model);
                    positions[i] = posi;
                }
            } else {
                //否，正常返回每行的数据
                positions = new int[column];
                for (int i = 0; i < column; i++) {
                    int posi = position * column + i;
                    T model = listDatas.get(posi);
                    models.add(model);
                    positions[i] = posi;
                }
            }
        }
        convert(holder, positions, models);
        return holder.getConvertView();
    }

    /**
     * 在子类中实现该方法
     *
     * @param holder
     *            列表项
     * @param positions
     * @param models
     *            每行的数据集，每行有几列就返回几个model。第一列对应数据下标0，一一对应，以此类推。
     *            不满列数的设置setVisibility(View.INVISIBLE);
     */
    public abstract void convert(MtjBaseViewHolder holder, int[] positions,
                                 List<T> models);

}