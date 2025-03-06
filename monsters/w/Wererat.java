package com.dndcombat.monsters.w;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList; 

public class Wererat extends Player {

    private static final long serialVersionUID = 1L;

    //TODO curse
    public Wererat() {
        super(new Creature());
        create();
    }

    public void create() {
        getCreature().setCreatureName("Wererat");
        setPlayersSide(false);
        getCreature().setImageUrl("/assets/monsters/MM/Wererat.webp");

        // Ability Scores
        str(10).dex(16).con(12).inte(11).wis(10).cha(8);

        // Saving Throws
        strSave(+0).dexSave(+3).conSave(+1).intSave(+0).wisSave(+0).chaSave(-1);

        // Combat Stats
        ac(13).init(+3).hp(60).speed(30).cr(2);

        // Size and Type
        size(RefList.creaturesizesmedium).type(RefList.creaturetypesmonstrosity);
 
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Bite(), Scratch()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(HandCrossbow(), HandCrossbow()));

        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));

        addAction(new Action("Scratch", RefList.actiontypesmainaction)
                .addAttack(Scratch()));

        addAction(new Action("Hand Crossbow", RefList.actiontypesmainaction)
                .addAttack(HandCrossbow()));

          
    }


    private Attack Bite() {
        Attack a = new Attack("Bite");
        a.setAttackBonus(5)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(2)
         .dieType(4)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypespiercing);
        
        return a;
    }

    private Attack Scratch() {
        Attack a = new Attack("Scratch");
        a.setAttackBonus(5)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesslashing);
        return a;
    }

    private Attack HandCrossbow() {
        Attack a = new Attack("Hand Crossbow (Humanoid or Hybrid Form Only)");
        a.setAttackBonus(5)
         .setAttackRange(30)
         .setLongRange(120)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypespiercing);
        return a;
    }
}