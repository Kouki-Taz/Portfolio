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
