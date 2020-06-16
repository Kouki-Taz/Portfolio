using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.EventSystems;

/*【内容】*/
/*・長押ししたときにCanvasを非表示にする*/

/*【使い方】*/
/*空のGameObjectかなにかにこのスクリプトをアタッチ*/
/*Inspectorから隠すオブジェクトを指定*/

/// <summary>
/// 長押しを取得してコールバックする（画面全域や画面の一部領域などでの判定に向いている）
/// 2017/7/6 Fantom (Unity 5.6.1p3)
/// http://fantom1x.blog130.fc2.com/blog-entry-251.html
///（使い方）
///・適当な GameObject にアタッチして、インスペクタから OnLongClick（引数なし）にコールバックする関数を登録すれば使用可。
///・またはプロパティ LongClickInput.IsLongClick をフレーム毎監視しても良い（こちらの場合は false も含まれる）。
///（仕様説明）
///・画面全体を(0,0)-(1,1)とし、有効領域内（Valid Area）で一定時間（Valid Time）押下されていたら認識する。
///・途中で有効領域外へ出たり、指を離したりしたときは無効。
///・はじめの指のみ認識（複数の指の場合、ピンチの可能性があるため無効とする）。
///・タッチデバイスを UNITY_ANDROID, UNITY_IOS としているので、他のデバイスも加えたい場合は #if の条件文にデバイスを追加する（Input.touchCount が取得できるもののみ）。
/// </summary>

public class CanvasControl : MonoBehaviour {

    
    public GameObject Canvas1;
    //public GameObject Canvas2;
    //public GameObject Canvas3;
    //public GameObject Canvas4;

    public bool CanvasVisible = true;
    

    //設定値
    public float validTime = 1.0f;      //長押しとして認識する時間（これより長い時間で長押しとして認識する）

    //認識する画面上の領域
    public Rect validArea = new Rect(0, 0, 1, 1);    //長押しとして認識する画面領域（0.0～1.0）[(0,0):画面左下, (1,1):画面右上]

    //Local Values
    Vector2 minPos = Vector2.zero;      //長押し認識ピクセル最小座標
    Vector2 maxPos = new Vector2(1, 1);//Vector2.one;       //長押し認識ピクセル最大座標
    float requiredTime;                 //長押し認識時刻（この時刻を超えたら長押しとして認識する）
    bool pressing;                      //押下中フラグ（単一指のみの取得にするため）

    bool isValid = false;               //フレーム毎判定用

    //自作、タッチした部分を中心に認識判定エリアを設定
    //Vector2 start_position = new Vector2(0, 0);
    Vector2 end_position_min = new Vector2(0, 0);
    Vector2 end_position_max = new Vector2(0, 0);
    private float hanni = 50.0f;

    //長押検出プロパティ（フレーム毎取得用）
    public bool IsLongClick
    {
        get { return isValid; }
    }


    //アクティブになったら、初期化する（アプリの中断などしたときはリセットする）
    void OnEnable()
    {
        pressing = false;
    }

    // Update is called once per frame
    void Update()
    {
        isValid = false;    //フレーム毎にリセット

        //他のUI触ってたら反応しないようにする
        /*
        //PC版
        if (EventSystem.current.IsPointerOverGameObject())
        {
            return;
        }*/
        //Android版
        if (Input.touchCount >= 1)
        {
            if (EventSystem.current.IsPointerOverGameObject(Input.GetTouch(0).fingerId))
            {
                return;
            }
        }

#if !UNITY_EDITOR && (UNITY_ANDROID || UNITY_IOS)   //タッチで取得したいプラットフォームのみ
        if (Input.touchCount == 1)  //複数の指は不可とする（※２つ以上の指の場合はピンチの可能性もあるため）
#endif
        {
            if (!pressing && Input.GetMouseButtonDown(0))   //押したとき（左クリック/タッチが取得できる）
            {
                Vector2 pos = Input.mousePosition;
                minPos.Set(validArea.xMin * Screen.width, validArea.yMin * Screen.height);//
                maxPos.Set(validArea.xMax * Screen.width, validArea.yMax * Screen.height);//
                if (minPos.x <= pos.x && pos.x <= maxPos.x && minPos.y <= pos.y && pos.y <= maxPos.y)   //認識エリア内
                {
                    pressing = true;
                    requiredTime = Time.time + validTime;

                    //
                    //start_position = new Vector2(pos.x, pos.y);
                    end_position_min = new Vector2(pos.x - hanni, pos.y - hanni);
                    end_position_max = new Vector2(pos.x + hanni, pos.y + hanni);
                }
            }
            if (pressing)      //既に押されている
            {
                if (Input.GetMouseButton(0))    //押下継続（※この関数は２つ以上タッチの場合、どの指か判別できない）
                {
                    if (requiredTime < Time.time)   //一定時間過ぎたら認識
                    {
                        Vector2 pos = Input.mousePosition;
                        if (minPos.x <= pos.x && pos.x <= maxPos.x && minPos.y <= pos.y && pos.y <= maxPos.y)   //認識エリア内
                        {
                            isValid = true;
                            
                            //ここ自作
                            if((pos.x < end_position_max.x) && (pos.x > end_position_min.x) && (pos.y < end_position_max.y) && (pos.y > end_position_min.y))
                            {





                                /*********************↓長押ししたときの処理↓*********************/
                                if (CanvasVisible == true)
                                {
                                    Canvas1.SetActive(false);
                                    CanvasVisible = false;
                                }
                                else
                                {
                                    Canvas1.SetActive(true);
                                    CanvasVisible = true;
                                }
                                /*********************↑長押ししたときの処理↑*********************/




                            }


                        }

                        pressing = false;   //長押し完了したら無効にする
                    }
                }
                else  //MouseButtonUp, MouseButtonDown
                {
                    pressing = false;
                }
            }
        }
#if !UNITY_EDITOR && (UNITY_ANDROID || UNITY_IOS)   //タッチで取得したいプラットフォームのみ
        else  //タッチが１つでないときは無効にする（※２つ以上の指の場合はピンチの可能性もあるため）
        {
            pressing = false;
        }
#endif
    }
}
