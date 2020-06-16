using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MMDModel : MonoBehaviour
{
    //変数
    private GameObject model;
    private string Modelname;

    //コンストラクタ
    public MMDModel(GameObject MMD)
    {
        //モデル設定
        model = MMD;

        //初期値設定
        model.transform.eulerAngles = new Vector3(0,0,0);
        model.transform.position = new Vector3(0, 0, 0);

        //MMD4MecanimAnimMorphHelperをモデルに追加
        model.AddComponent<MMD4MecanimAnimMorphHelper>();

        //モデルに各種データをセット
        model.GetComponent<Animator>().runtimeAnimatorController = Resources.Load<RuntimeAnimatorController>("Animation/AC_motion");
        model.GetComponent<MMD4MecanimModel>().physicsEngine = MMD4MecanimModelImpl.PhysicsEngine.BulletPhysics;

        /*
        //表情リスト作成
        MMD4MecanimAnimMorphHelper.Anim[] animations = new MMD4MecanimAnimMorphHelper.Anim[ExpressionName.Length];
        for (int i = 0; i < animations.Length; i++)
        {
            animations[i] = new MMD4MecanimAnimMorphHelper.Anim();
            animations[i].animName = ExpressionName[i];
            animations[i].animFile = Resources.Load<TextAsset>("Music&Motion/" + ExpressionName[i] + ".anim");
            animations[i].audioClip = Resources.Load<AudioClip>("Music&Motion/" + MusicName[i + 1]);
        }
        model.GetComponent<MMD4MecanimAnimMorphHelper>().animList = animations;
        */
    }
    //デストラクタ
    ~MMDModel()
    {
        //モデルをアンロード
        Destroy(model);
    }



    //ゲッター・セッター
    public void SetPosition(Vector3 x)
    {
        model.transform.position = x;
    }
    public Vector3 GetPosition()
    {
        return model.transform.position;
    }
    public void SetRotation(Vector3 x)
    {
        model.transform.eulerAngles = x;
    }
    public Vector3 GetRotation()
    {
        return model.transform.eulerAngles;
    }
}
