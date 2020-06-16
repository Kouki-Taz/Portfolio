using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class ObjectManager : MonoBehaviour {

    /*データ*/
    private string[] MMDName = { "KakyoinChieri", "YamatoIori" };
    private string[] CharacterName = {"花京院ちえり" , "ヤマトイオリ"};
    private string[] StageName = { "Default", "Sweet Room", "清水の花道", "夏が過ぎた庭" };
    private string[] ExpressionName = { "[A]ddiction", "FRIENDS","太陽系デスコ","極楽浄土","好き雪本気マジック",
        "桃源恋歌","トキヲ・ファンカ","BadEndNight","テレキャスタービーボーイ" };
    private string[] MusicName = { "None", "【A】ddiction", "FRIENDS","太陽系デスコ","極楽浄土","好き!雪!本気マジック!",
        "桃源恋歌","トキヲ・ファンカ","Bad∞End∞Night","テレキャスタービーボーイ" };
    private string[] TriggerName = { "Addiction", "Friends","Disco","Gokurakujoudo","MaziMagic",
        "Tougenrenka", "Tokiwofanka","BadEndNight", "TBboy" };
    private int[] MusicRotation = { 0,0,0,-90,0,0,0,0,0};

    private GameObject[] MMDModel;
    private GameObject[] Stage;
    
    /*カメラ*/
    private GameObject Camera;
    private GameObject Camera_P;
    private GameObject Camera_P_P;

    /*ドロップダウン*/
    private Dropdown Dropdown_MMD;
    private Dropdown Dropdown_Stage;
    private Dropdown Dropdown_Music;
    private Dropdown Dropdown_Resolution;

    /*ボタン*/
    private GameObject PlayButton;
    private GameObject StopButton;
    private GameObject ChangeMMDButton;
    private GameObject ChangeStageButton;
    private GameObject ChangeResolutionButton;
    private GameObject RotationButton;
    private GameObject MMDResetButton;
    private GameObject CameraResetButton;
    private GameObject InformationButton;

    /*テキストパネル*/
    private GameObject InformationText;
    private GameObject InformationPanel;

    /*キャンバス*/
    private GameObject MainCanvas;

    /*MMDの初期位置と初期回転*/
    public Vector3 InitPos_MMD = new Vector3(0, 0, 0);
    public Vector3 InitRot_MMD = new Vector3(0, 0, 0);

    /*カメラの初期位置と初期回転*/
    public Vector3 InitPos_Camera = new Vector3(0, 1, 2);
    public Vector3 InitRot_Camera = new Vector3(0, 180, 0);
    public Vector3 InitPos_Camera_P = new Vector3(0, 0, 0);
    public Vector3 InitRot_Camera_P = new Vector3(0, 0, 0);
    public Vector3 InitPos_Camera_P_P = new Vector3(0, 0, 0);
    public Vector3 InitRot_Camera_P_P = new Vector3(0, 0, 0);

    //デフォルトの画質
    private int default_width;
    private int default_height;

    //端末の解像度
    private int default_resolution_x = 640;
    private int default_resolution_y = 1136;//Galaxy S7 edge は 1080x1920

    //UIスケール倍率
    private float scale_ratio = 0.5f;//Galaxy S7 edge を 1 とする

    //UIのサイズ
    private Vector2 DropDownSize = new Vector2(400, 70);
    private Vector2 DropDownSize2 = new Vector2(300, 70);
    private Vector2 ChangeButtonSize = new Vector2(80, 80);
    private Vector2 PlayButtonSize = new Vector2(200, 200);
    private Vector2 RotationButtonSize = new Vector2(100, 100);

    //文字サイズ
    private int Dropdown_FontSize = 30;
    private int ResetButton_FontSize = 24;
    private int InformationText_FontSize = 20;

    //レイアウトに用いる変数
    private int SideMargin = 20;
    private int VerticalMargin = 20;
    private int WidthBetweenUIs = 20;
    private int HeightBetweenUIs = 20;

    //オプション値
    private int FPS_value = 30;
    //private float pixelsPerUnit = 100.0f;

    //時間あるときにドロップダウンのコンテンツサイズ変更も


    //currentMMD



    /**************************************音楽再生処理*************************************/
    public void Play(GameObject MMD)
    {
        PlayButton.SetActive(false);
        StopButton.SetActive(true);

        for(int i = 0; i < ExpressionName.Length; i++)
        {
            if (Dropdown_Music.value == (i + 1))
            {
                var MMD_hyoujou = MMD.GetComponent<MMD4MecanimAnimMorphHelper>();

                MMD.transform.position = InitPos_MMD + new Vector3(0, 0, 0);
                MMD.transform.eulerAngles = InitRot_MMD + new Vector3(0, MusicRotation[i], 0);
                MMD.GetComponent<Animator>().SetTrigger(TriggerName[i]);
                MMD_hyoujou.playingAnimName = "";
                MMD_hyoujou.animName = ExpressionName[i];

                break;
            }
        }
    }
    public void Stop(GameObject MMD)
    {
        var MMD_hyoujou = MMD.GetComponent<MMD4MecanimAnimMorphHelper>();

        StopButton.SetActive(false);
        PlayButton.SetActive(true);

        MMD.transform.position = InitPos_MMD;
        MMD.transform.eulerAngles = InitRot_MMD;
        MMD.GetComponent<Animator>().SetTrigger("Wait");
        MMD_hyoujou.playingAnimName = "";
    }
    public void ChangeMusic()
    {
        for (int i = 0; i < MMDModel.Length; i++)
        {
            if (MMDModel[i].activeSelf == true)
            {
                if (Dropdown_Music.value == 0 || StopButton.activeSelf == true) Stop(MMDModel[i]);
                else if (Dropdown_Music.value > 0) Play(MMDModel[i]);
                break;
            }
        }
    }
    //キャラ更新ボタンを押した際の処理
    public void Reload()
    {
        StopButton.SetActive(true);
        ChangeMusic();
    }


    

    /*******************************チェンジ処理**********************************/
    public void ChangeMMD()
    {
        Reload();
        if (Dropdown_MMD)
        {
            //一度すべてOFF
            for (int i = 0; i < MMDModel.Length; i++)
            {
                MMDModel[i].SetActive(false);
            }
            //選択したものだけON
            MMDModel[Dropdown_MMD.value].SetActive(true);
        }
    }
    public void ChangeStage()
    {
        if (Dropdown_Stage)
        {
            //一度すべてOFF
            for (int i = 0; i < Stage.Length; i++)
            {
                Stage[i].SetActive(false);
            }
            //選択したものだけON
            Stage[Dropdown_Stage.value].SetActive(true);
        }
    }

    


    /********************************リセット処理*************************************/
    //MMDリセット
    public void ResetMMD()
    {
        for (int i = 0; i < MMDModel.Length; i++)
        {
            if (MMDModel[i].activeSelf == true)
            {
                MMDModel[i].transform.eulerAngles = InitRot_MMD;
                MMDModel[i].transform.position = InitPos_MMD;
                break;
            }
        }
    }
    //カメラリセット
    public void ResetCamera()
    {
        /*この順でないとうまくいかない*/

        //親の親オブジェクトをリセット
        Camera_P_P.transform.position = InitPos_Camera_P_P;
        Camera_P_P.transform.eulerAngles = InitRot_Camera_P_P;
        //親オブジェクトもリセット
        Camera_P.transform.position = InitPos_Camera_P;
        Camera_P.transform.eulerAngles = InitRot_Camera_P;
        //オブジェクトリセット
        Camera.transform.position = InitPos_Camera;
        Camera.transform.eulerAngles = InitRot_Camera;
    }



    /********************************その他ボタン機能*************************************/
    //テキストパネル表示
    public void InformationVisible()
    {
        if (InformationPanel.activeSelf == false)
            InformationPanel.SetActive(true);
        else
            InformationPanel.SetActive(false);
    }

    //画面回転
    public void Rotation()
    {
        //縦画面の時
        if (Screen.orientation == ScreenOrientation.Portrait)
            Screen.orientation = ScreenOrientation.LandscapeLeft;
        else
            Screen.orientation = ScreenOrientation.Portrait;

        //UIサイズ変更
        AdjustScale();//ここCanvasのInspectorで解像度設定したからいらないかも？

        //デバッグ用
        PrintDebugMessage();
    }

    //解像度変更
    public void SetResolution()
    {
        //Galaxy S7 edge は 1440x2560(?)
        int width;
        int height;
        if (Screen.orientation == ScreenOrientation.Portrait)
        {
            width = Screen.currentResolution.width;
            height = Screen.currentResolution.height;
        }
        else
        {
            width = Screen.currentResolution.height;
            height = Screen.currentResolution.width;
        }
        float screenRate = 1;
        bool fullscreen = false;
        int preferredRefreshRate = 30;
        
        if(Dropdown_Resolution.value == 0)
            screenRate = (float)default_height / default_height;
        else if (Dropdown_Resolution.value == 1)
            screenRate = 0.9f;
        else if (Dropdown_Resolution.value == 2)
            screenRate = 0.7f;

        if (screenRate > 1)
            screenRate = 1;
        
        if (Screen.orientation == ScreenOrientation.Portrait)
        {
            width = (int)(default_width * screenRate);
            height = (int)(default_height * screenRate);
        }
        else
        {
            width = (int)(default_height * screenRate);
            height = (int)(default_width * screenRate);
        }

        //実際の解像度変更処理は現在のフレーム終了時に行われるらしい
        Screen.SetResolution(width, height, fullscreen, preferredRefreshRate);

        //UIサイズ変更処理はCanvasのInspectorで解像度変更対応してるからいらない

        //デバッグ用
        PrintDebugMessage();
    }

    //ドロップダウン設定
    public void SetDropdown()
    {
        List<string> MMDList = new List<string>();
        List<string> StageList = new List<string>();
        List<string> MusicList = new List<string>();

        for (int i = 0; i < CharacterName.Length; i++)
            MMDList.Add(CharacterName[i]);
        for (int i = 0; i < StageName.Length; i++)
            StageList.Add(StageName[i]);
        for (int i = 0; i < MusicName.Length; i++)
            MusicList.Add(MusicName[i]);

        Dropdown_MMD.AddOptions(MMDList);
        Dropdown_MMD.value = 0;
        Dropdown_Stage.AddOptions(StageList);
        Dropdown_Stage.value = 0;
        Dropdown_Music.AddOptions(MusicList);
        Dropdown_Music.value = 0;
    }

    //各MMDにモーション等追加
    public void SetMMDOptions()
    {

        //表情リスト作成
        MMD4MecanimAnimMorphHelper.Anim[] animations = new MMD4MecanimAnimMorphHelper.Anim[ExpressionName.Length];
        for (int i = 0; i < animations.Length; i++)
        {
            //1曲ごとのforループ
            animations[i] = new MMD4MecanimAnimMorphHelper.Anim();
            animations[i].animName = ExpressionName[i];
            animations[i].animFile = Resources.Load<TextAsset>("Music&Motion/" + ExpressionName[i] + ".anim");
            animations[i].audioClip = Resources.Load<AudioClip>("Music&Motion/" + MusicName[i+1]);
        }
        //各MMDにオプション追加
        for (int i = 0; i < MMDModel.Length; i++)
        {
            //MMDごとのforループ
            MMDModel[i].GetComponent<Animator>().runtimeAnimatorController = Resources.Load<RuntimeAnimatorController>("Animation/AC_motion");
            MMDModel[i].GetComponent<MMD4MecanimModel>().physicsEngine = MMD4MecanimModelImpl.PhysicsEngine.BulletPhysics;
            MMDModel[i].GetComponent<MMD4MecanimAnimMorphHelper>().animList = animations;
        }

    }

    //UIのサイズ変更
    public void AdjustScale()
    {
        //横画面対応
        int screenWidth;
        int screenHeight;
        if(Screen.orientation == ScreenOrientation.Portrait)
        {
            screenWidth = Screen.width;
            screenHeight = Screen.height;
        }
        else
        {
            screenWidth = Screen.height;
            screenHeight = Screen.width;
        }


        //【サイズ設定】
        //Canvasサイズ変更
        MainCanvas.GetComponent<CanvasScaler>().referenceResolution = new Vector2(default_resolution_x, default_resolution_y);
        MainCanvas.GetComponent<RectTransform>().sizeDelta = new Vector2(default_resolution_x, default_resolution_y);

        //UIサイズ変更
        //パネル
        float InformationPanel_Width = screenWidth * 0.8f;
        float InformationPanel_Height = screenHeight * 0.8f;
        InformationPanel.GetComponent<RectTransform>().sizeDelta = new Vector2(InformationPanel_Width, InformationPanel_Height);
        //ドロップダウン
        Dropdown_MMD.GetComponent<RectTransform>().sizeDelta = DropDownSize;
        Dropdown_Stage.GetComponent<RectTransform>().sizeDelta = DropDownSize;
        Dropdown_Music.GetComponent<RectTransform>().sizeDelta = DropDownSize;
        Dropdown_Resolution.GetComponent<RectTransform>().sizeDelta = DropDownSize2;
        //ボタン
        PlayButton.transform.Find("ChangeMusic_Button").gameObject.GetComponent<RectTransform>().sizeDelta = PlayButtonSize;
        StopButton.transform.Find("MusicStop_Button").gameObject.GetComponent<RectTransform>().sizeDelta = PlayButtonSize;
        ChangeMMDButton.GetComponent<RectTransform>().sizeDelta = ChangeButtonSize;
        ChangeStageButton.GetComponent<RectTransform>().sizeDelta = ChangeButtonSize;
        ChangeResolutionButton.GetComponent<RectTransform>().sizeDelta = ChangeButtonSize;
        RotationButton.GetComponent<RectTransform>().sizeDelta = RotationButtonSize;
        MMDResetButton.GetComponent<RectTransform>().sizeDelta = DropDownSize2;
        CameraResetButton.GetComponent<RectTransform>().sizeDelta = DropDownSize2;
        InformationButton.GetComponent<RectTransform>().sizeDelta = RotationButtonSize;

        //文字サイズ設定
        Dropdown_MMD.transform.Find("Label").GetComponent<Text>().fontSize = Dropdown_FontSize;
        Dropdown_Stage.transform.Find("Label").GetComponent<Text>().fontSize = Dropdown_FontSize;
        Dropdown_Music.transform.Find("Label").GetComponent<Text>().fontSize = Dropdown_FontSize;
        Dropdown_Resolution.transform.Find("Label").GetComponent<Text>().fontSize = Dropdown_FontSize;
        MMDResetButton.transform.Find("Text").GetComponent<Text>().fontSize = ResetButton_FontSize;
        CameraResetButton.transform.Find("Text").GetComponent<Text>().fontSize = ResetButton_FontSize;
        InformationPanel.transform.Find("InformationText").GetComponent<Text>().fontSize = InformationText_FontSize;

        //【スケール変更】
        //ドロップダウン
        Dropdown_MMD.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        Dropdown_Stage.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        Dropdown_Music.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        Dropdown_Resolution.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        //ボタン
        PlayButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        StopButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        ChangeMMDButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        ChangeStageButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        ChangeResolutionButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        RotationButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        MMDResetButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        CameraResetButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);
        InformationButton.transform.localScale = new Vector3(scale_ratio, scale_ratio, scale_ratio);


        //【配置】
        //左上
        float Dropdown_MMD_Width = Dropdown_MMD.transform.localScale.x * DropDownSize.x;
        float Dropdown_MMD_Height = Dropdown_MMD.transform.localScale.y * DropDownSize.y;
        float Dropdown_MMD_PositionX = screenWidth / 2 - Dropdown_MMD_Width / 2 - SideMargin;
        float Dropdown_MMD_PositionY = screenHeight / 2 - Dropdown_MMD_Height / 2 - VerticalMargin;
        Dropdown_MMD.transform.localPosition = new Vector3(-Dropdown_MMD_PositionX, Dropdown_MMD_PositionY, 0f);
        
        float Dropdown_Stage_PositionY = Dropdown_MMD_PositionY  - (Dropdown_MMD_Height + HeightBetweenUIs);//()はドロップダウンアイコン1つ分の意味
        Dropdown_Stage.transform.localPosition = new Vector3(-Dropdown_MMD_PositionX, Dropdown_Stage_PositionY, 0f);

        float Dropdown_Music_PositionY = Dropdown_Stage_PositionY - (Dropdown_MMD_Height + HeightBetweenUIs);//()はドロップダウンアイコン1つ分の意味
        Dropdown_Music.transform.localPosition = new Vector3(-Dropdown_MMD_PositionX, Dropdown_Music_PositionY, 0f);


        float ChangeMMDButton_Width = ChangeMMDButton.transform.localScale.x * ChangeButtonSize.x;
        float ChangeMMDButton_PositionX = Dropdown_MMD_PositionX - Dropdown_MMD_Width / 2 - ChangeMMDButton_Width / 2 - WidthBetweenUIs;
        ChangeMMDButton.transform.localPosition = new Vector3(-ChangeMMDButton_PositionX, Dropdown_MMD_PositionY, 0f);
        
        ChangeStageButton.transform.localPosition = new Vector3(-ChangeMMDButton_PositionX, Dropdown_Stage_PositionY, 0f);

        //右上
        float Dropdown_Resolution_Width = Dropdown_Resolution.transform.localScale.x * DropDownSize2.x;
        float Dropdown_Resolution_PositionX = screenWidth / 2 - Dropdown_Resolution_Width / 2 - SideMargin - (ChangeMMDButton_Width + WidthBetweenUIs);//()は更新ボタンアイコン1つ分の意味
        Dropdown_Resolution.transform.localPosition = new Vector3(Dropdown_Resolution_PositionX, Dropdown_Music_PositionY, 0f);
        
        float ChangeResolutionButton_PositionX = screenWidth / 2  - ChangeMMDButton_Width / 2 - SideMargin;//()は更新ボタンアイコン1つ分の意味
        ChangeResolutionButton.transform.localPosition = new Vector3(ChangeResolutionButton_PositionX, Dropdown_Music_PositionY, 0f);

        //右下
        float MMDResetButton_Width = MMDResetButton.transform.localScale.x * DropDownSize2.x;
        float MMDResetButton_PositionX = screenWidth / 2 - MMDResetButton_Width / 2 - SideMargin;
        MMDResetButton.transform.localPosition = new Vector3(MMDResetButton_PositionX, -Dropdown_MMD_PositionY, 0f);
        
        CameraResetButton.transform.localPosition = new Vector3(MMDResetButton_PositionX, -Dropdown_Stage_PositionY, 0f);

        //中心
        float PlayButton_Height = PlayButton.transform.localScale.y * PlayButtonSize.x;
        float PlayButton_PositionY = screenHeight / 2 - (PlayButton_Height / 2) * 3;
        PlayButton.transform.localPosition = new Vector3(0f, -PlayButton_PositionY, 0f);
        StopButton.transform.localPosition = PlayButton.transform.localPosition;

        //その他
        float RotationButton_Width = RotationButton.transform.localScale.x * RotationButtonSize.x;
        float RotationButton_Height = RotationButton.transform.localScale.y * RotationButtonSize.y;
        float RotationButton_PositionX = screenWidth / 2 - RotationButton_Width / 2 - SideMargin * 2;
        float RotationButton_PositionY = screenHeight / 2 - RotationButton_Height / 2 - VerticalMargin * 2;
        RotationButton.transform.localPosition = new Vector3(RotationButton_PositionX, RotationButton_PositionY, 0f);

        float InformationButton_PositionX = RotationButton_PositionX - (RotationButton_Width + WidthBetweenUIs);
        InformationButton.transform.localPosition = new Vector3(InformationButton_PositionX, RotationButton_PositionY, 0f);

    }

    //オブジェクト紐づけ
    public void ObjectFind()
    {
        MMDModel = new GameObject[MMDName.Length];
        for (int i = 0; i < MMDModel.Length; i++)
            MMDModel[i] = GameObject.Find("MMDModels").transform.Find(MMDName[i]).gameObject;

        Stage = new GameObject[StageName.Length];
        for (int i = 0; i < Stage.Length; i++)
            Stage[i] = GameObject.Find("Stages").transform.Find(StageName[i]).gameObject;

        Camera = GameObject.Find("Camera");
        Camera_P = GameObject.Find("CameraFixObject");
        Camera_P_P = GameObject.Find("ObservationCameraObject");

        Dropdown_MMD = GameObject.Find("MMD_Dropdown").GetComponent<Dropdown>();
        Dropdown_Stage = GameObject.Find("Stage_Dropdown").GetComponent<Dropdown>();
        Dropdown_Music = GameObject.Find("Music_Dropdown").GetComponent<Dropdown>();
        Dropdown_Resolution = GameObject.Find("Resolution_Dropdown").GetComponent<Dropdown>();

        PlayButton = GameObject.Find("Canvas_play");
        StopButton = GameObject.Find("Canvas_stop");
        ChangeMMDButton = GameObject.Find("ChangeMMD_Button");
        ChangeStageButton = GameObject.Find("ChangeStage_Button");
        ChangeResolutionButton = GameObject.Find("ChangeResolution_Button");
        RotationButton = GameObject.Find("Rotation_Button");
        MMDResetButton = GameObject.Find("MMDReset_Button");
        CameraResetButton = GameObject.Find("CameraReset_Button");
        InformationButton = GameObject.Find("Information_Button");

        InformationText = GameObject.Find("InformationText");
        InformationPanel = GameObject.Find("InformationPanel");

        MainCanvas = GameObject.Find("Canvas");
    }

    //【デバッグ用】
    public void PrintDebugMessage()
    {
        InformationText.GetComponent<Text>().text = "";
        string debug_text1 = "default(" + default_width + ", " + default_height + ")";
        string debug_text2 = "current(" + Screen.width + ", " + Screen.height + ")";
        string debug_text3 = "MMDDropdown(" + Dropdown_MMD.transform.localPosition.x + ", " + Dropdown_MMD.transform.localPosition.y + ")";
        string debug_text4 = "PlayButton(" + PlayButton.transform.localPosition.x + ", " + PlayButton.transform.localPosition.y + ")";
        string debug_text5 = "ScreenRate : " + ((float)Screen.height / default_height);
        string debug_text6 = "ScreenOrientation : " + Screen.orientation;
        InformationText.GetComponent<Text>().text = debug_text1 + "\n" + debug_text2 + "\n" + debug_text3 + "\n" + debug_text4 + "\n" + debug_text5
            + "\n" + debug_text6;
    }

    //オプション設定
    public void SetOptions()
    {
        Application.targetFrameRate = FPS_value; //FPS設定
        Screen.orientation = ScreenOrientation.Portrait;
    }


    void Awake()
    {
        //オブジェクト紐づけ
        ObjectFind();

        //各MMDにモーション等追加
        SetMMDOptions();//Awake()内でなければうまく動作しない(start内ではダメ)
    }

    void Start () {

        //デフォルトの画質保存
        default_width = Screen.width;
        default_height = Screen.height;

        //オプション設定
        SetOptions();

        //オブジェクト紐づけ
        //ObjectFind();

        //ドロップダウン設定
        SetDropdown();

        //UIサイズ変更
        AdjustScale();

        //各MMDにモーション等追加
        //SetMMDOptions();

        //その他開始時の処理
        StopButton.SetActive(false);
        InformationPanel.SetActive(false);


        //デバッグ用
        PrintDebugMessage();
    }


    void Update () {
        //戻るボタンを押したらアプリを終了させる
        if (Application.platform == RuntimePlatform.Android){
            // エスケープキー取得
            if (Input.GetKeyDown(KeyCode.Escape)){
                // アプリケーション終了
                Application.Quit();
                return;
            }
        }

        //InformationPanel非表示
        if(InformationPanel.activeSelf == true)
        {
            //画面をタッチしている判定
            if (Input.touchCount >= 1 || Input.GetMouseButton(0))
            {
                //IP以外の場所をタッチしている判定
                /*
                EventSystem eventSystem = EventSystem.current;
                if (!eventSystem.IsPointerOverGameObject(Input.touchCount))
                {
                    InformationPanel.SetActive(false);
                }

                if (!eventSystem.IsPointerOverGameObject())
                {
                    InformationPanel.SetActive(false);
                }
                */
                PointerEventData pointer = new PointerEventData(EventSystem.current);
                List<RaycastResult> results = new List<RaycastResult>();
                // マウスポインタの位置にレイ飛ばし、ヒットしたものを保存
                pointer.position = Input.mousePosition;
                EventSystem.current.RaycastAll(pointer, results);
                int touchFlag = 0;
                // ヒットしたUIの名前
                foreach (RaycastResult target in results)
                {
                    Debug.Log(target.gameObject.name);
                    if(target.gameObject == InformationPanel)
                    {
                        touchFlag = 1;
                        break;
                    }
                }

                if(touchFlag == 0)
                    InformationPanel.SetActive(false);

            }
        }
    }

}
