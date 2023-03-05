package com.ryokusasa.cut_in_app.Activity.SelCutIn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.ryokusasa.cut_in_app.Activity.CutInEditer.CutInEditerActivity;
import com.ryokusasa.cut_in_app.CutIn.CutIn;
import com.ryokusasa.cut_in_app.CutIn.CutInHolder;
import com.ryokusasa.cut_in_app.ImageUtils.ImageData;
import com.ryokusasa.cut_in_app .R;
import com.ryokusasa.cut_in_app.UtilCommon;

/**
 * Created by fripl on 2017/12/25.
 * カットイン選択アクティビティ
 */

//TODO: タッチでプレビュー　長押しで選択
public class SelCutInActivity extends AppCompatActivity {

    private static final String TAG = "SelCutInActivity";

    private static final int CUT_IN_SET = 0;
    private static final int CUT_IN_EDIT = 1;
    private static final int CUT_IN_DELETE = 2;
    private static final int CUT_IN_TITLE_CHANGE = 3;
    private static final int CUT_IN_SET_THUMBNAIL = 4;

    private CutInAdapter cutInAdapter;
    private CutInHolder cutInHolder;

    private UtilCommon utilCommon;

    private ActivityResultLauncher<Intent> thumbnailSelectLauncher;

    private int selCutInId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sel_cut_in_activity);
        setSupportActionBar(findViewById(R.id.cut_in_toolbar));

        utilCommon = (UtilCommon)getApplication();

        //画像選択用ActivityResult
        thumbnailSelectLauncher = this.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK) {
                utilCommon.cutInList.get(selCutInId).imageData = new ImageData(result.getData().getData(), ImageData.EXTERNAL_STORAGE);
            }
        });



        //カットインアダプター追加処理
        cutInAdapter = new CutInAdapter(this, 0, utilCommon.cutInList);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(cutInAdapter);
        gridView.setOnItemLongClickListener(onItemLongClickListener);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Log.i(TAG, "onItemClick:" + position);
            utilCommon.play(position);
        });

        //ホルダー取得
        cutInHolder = utilCommon.cutInHolderList.get(getIntent().getIntExtra("id", 0));

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

    private final AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            selCutInId = position;

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
                            finish();
                            break;

                        case CUT_IN_EDIT:    //TODO カットイン編集
                            //startCutInEditerActivity(position);
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
                                    utilCommon.removeCutIn(utilCommon.cutInList.remove(position));
                                    cutInAdapter.notifyDataSetChanged();
                                }
                            });
                            builder.create().show();
                            break;
                        case CUT_IN_TITLE_CHANGE:
                            final EditText editView = new EditText(SelCutInActivity.this);
                            editView.setText(((CutIn)cutInAdapter.getItem(position)).title);
                            final AlertDialog.Builder editBuilder = new AlertDialog.Builder(SelCutInActivity.this);
                            editBuilder.setView(editView);
                            editBuilder.setTitle("タイトル入力");
                            editBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!editView.getText().toString().equals("")){
                                        ((CutIn)cutInAdapter.getItem(position)).title = editView.getText().toString();
                                        cutInAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            editBuilder.create().show();
                            break;

                        case CUT_IN_SET_THUMBNAIL:
                            //TODO: サムネイル選択
                            startImageSelectWindow();
                            break;

                    }
                }
            });
            builder.create().show();
            return true;
        }
    };

    //カットイン編集アクティビティ起動
    private void startCutInEditerActivity(int cutInId){
        Intent intent = new Intent(SelCutInActivity.this, CutInEditerActivity.class);
        //選択カットイン番号を伝える
        Bundle bundle = new Bundle();
        bundle.putInt("selCutInId", cutInId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //画像選択ウィンドウ表示
    private void startImageSelectWindow(){
        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        thumbnailSelectLauncher.launch(intent);
    }
}