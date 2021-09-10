package com.ryokusasa.w3033901.cut_in_app_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutIn;
import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutInAdapter;
import com.ryokusasa.w3033901.cut_in_app_2.CutIn.CutInHolder;
import com.ryokusasa.w3033901.cut_in_app_2.CutInEditer.CutInEditerActivity;

/**
 * Created by fripl on 2017/12/25.
 * カットイン選択アクティビティ
 */

public class SelCutInActivity extends AppCompatActivity {

    private static final String TAG = "SelCutInActivity";

    private static final int CUT_IN_SET = 0;
    private static final int CUT_IN_EDIT = 1;
    private static final int CUT_IN_DELETE = 2;
    private static final int CUT_IN_TITLE_CHANGE = 3;

    private CutInAdapter cutInAdapter;
    private CutInHolder cutInHolder;

    private UtilCommon utilCommon;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_cut_in_activity);
        setSupportActionBar((Toolbar)findViewById(R.id.cut_in_toolbar));

        utilCommon = (UtilCommon)getApplication();

        //カットインアダプター追加処理
        cutInAdapter = new CutInAdapter(this, 0, MainActivity.getCutInList());
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(cutInAdapter);
        gridView.setOnItemClickListener(onCutInClick);
        cutInAdapter.setOnImageClickListener((position) -> utilCommon.play(position));

        //ホルダー取得
        cutInHolder = MainActivity.getCutInHolderList().get(getIntent().getIntExtra("id", 0));

    }

    @Override
    protected void onResume() {
        super.onResume();
        cutInAdapter.notifyDataSetChanged();
    }

    //オプションメニュー作成時
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //メニューレイアウト適用
        getMenuInflater().inflate(R.menu.sel_cut_in_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id){
            case R.id.newCutIn:    //TODO カットイン新規作成
                startCutInEditerActivity(-1);
                break;
        }
        return true;
    }

    private AdapterView.OnItemClickListener onCutInClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            Log.i(TAG, "onItemClick:" + position);

            /* 設定及び編集画面選択ウィンドウ表示 */
            AlertDialog.Builder builder = new AlertDialog.Builder(SelCutInActivity.this);
            builder.setTitle(R.string.control_cutin_menu);
            builder.setItems(R.array.control_cutin_menu_items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case CUT_IN_SET: //カットイン設定
                            // カットインをセット
                            cutInHolder.setCutIn((CutIn)cutInAdapter.getItem(position));

                            //TODO カットインセット保存
                            //new CutInDataManager(getApplicationContext()).cutInSetListSave(new ArrayList<CutInSet>(CutInService.cutInSetList));

                            finish();
                            break;

                        case CUT_IN_EDIT:    //TODO カットイン編集
                            startCutInEditerActivity(position);
                            break;
                        case CUT_IN_DELETE:  //カットイン削除
                            //カットイン削除確認ウィンドウ
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelCutInActivity.this);
                            builder.setTitle("注意");
                            builder.setMessage("このカットインを削除しますがよろしいですか？");
                            builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //キャンセル時何もせず閉じる
                                }
                            });
                            builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //OK時カットイン削除
                                    MainActivity.removeCutIn(MainActivity.getCutInList().remove(position));
                                    cutInAdapter.notifyDataSetChanged();
                                }
                            });
                            builder.create().show();
                            break;
                        case CUT_IN_TITLE_CHANGE:
                            final EditText editView = new EditText(SelCutInActivity.this);
                            editView.setText(((CutIn)cutInAdapter.getItem(position)).getTitle());
                            final AlertDialog.Builder editBuilder = new AlertDialog.Builder(SelCutInActivity.this);
                            editBuilder.setView(editView);
                            editBuilder.setTitle("タイトル入力");
                            editBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!editView.getText().toString().equals("")){
                                        ((CutIn)cutInAdapter.getItem(position)).setTitle(editView.getText().toString());
                                        cutInAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            editBuilder.create().show();
                            break;

                    }
                }
            });
            builder.create().show();
        }
    };

    private void startCutInEditerActivity(int cutInId){
        Intent intent = new Intent(SelCutInActivity.this, CutInEditerActivity.class);
        //選択カットイン番号を伝える
        Bundle bundle = new Bundle();
        bundle.putInt("selCutInId", cutInId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}