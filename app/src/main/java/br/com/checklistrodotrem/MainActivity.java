package br.com.checklistrodotrem;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.*;import android.net.Uri;import android.provider.MediaStore;import android.view.*;import android.widget.*;import androidx.core.content.FileProvider;import org.json.*;import java.io.*;import java.text.*;import java.util.*;

public class MainActivity extends Activity {
    final int NAVY=Color.rgb(6,59,110), BLUE=Color.rgb(11,115,209), GREEN=Color.rgb(18,168,90), RED=Color.rgb(229,57,53), BG=Color.rgb(244,247,250), TEXT=Color.rgb(23,50,77), MUTED=Color.rgb(109,124,139);
    LinearLayout root, content; DatabaseHelper db; ArrayList<RadioGroup> groups=new ArrayList<>(); ArrayList<EditText> obs=new ArrayList<>();
    EditText driver,date,tractor,trailer1,trailer2; Spinner type; int current=0;
    String[] qs={"Descanso e sono apropriados? Apto a trabalhar com CT?","Controle periódico de manutenção?","Quinta (5ª) roda e pino rei?","Buzina?","Conexões e válvulas?","Qual tipo de válvula possui?","Verificação se a válvula API está em posição de fechada?","Sistema de carregamento?","Guarda corpo?","Cinto de segurança?","Rotograma?","Manete da carreta?","Fluido hidráulico?","Nível de água do radiador?","Nível do óleo?","Computador de bordo?","Parabrisa, janelas e espelhos?","Pneus e rodas?","Triângulo?","Marcação de compartimentos?","Pontos de aterramento?","Sistema de freio?","Alarme de ré?","Extintores?","Calço?","Tanques (vazamentos)?","Todas as luzes?","Documentos do veículo?","Bateria?","Placas do veículo estão OK?","Faixa refletiva?","Kit emergência?","Simbologia de risco?","EPIs?"};
    String[] types=new String[34];
    public void onCreate(Bundle b){super.onCreate(b);db=new DatabaseHelper(this);for(int i=0;i<34;i++)types[i]="padrao";types[0]="sim_nao";types[5]="valvula";types[7]="carregamento";showHome();}
    TextView tv(String s,float sp,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(color);t.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);t.setPadding(0,4,0,4);return t;}
    Button btn(String s,int color){Button b=new Button(this);b.setText(s);b.setTextColor(Color.WHITE);b.setTextSize(15);b.setAllCaps(false);GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(22);b.setBackground(g);b.setPadding(20,4,20,4);return b;}
    LinearLayout page(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG);setContentView(root);return root;}
    void top(String title){LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setPadding(18,10,18,10);bar.setBackgroundColor(NAVY);TextView t=tv(title,20,Color.WHITE);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);bar.addView(t,new LinearLayout.LayoutParams(0,58,1));root.addView(bar);}
    ScrollView scroll(){ScrollView s=new ScrollView(this);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(18,16,18,28);s.addView(content);root.addView(s,new LinearLayout.LayoutParams(-1,0,1));return s;}
    void showHome(){page(); LinearLayout head=new LinearLayout(this);head.setOrientation(LinearLayout.VERTICAL);head.setGravity(Gravity.CENTER);head.setPadding(20,28,20,24);head.setBackgroundResource(br.com.checklistrodotrem.R.drawable.bg_header);ImageView im=new ImageView(this);im.setImageResource(R.drawable.ic_truck);head.addView(im,new LinearLayout.LayoutParams(-1,110));TextView title=tv("CHECKLIST RODOTREM / LS",25,Color.WHITE);title.setGravity(Gravity.CENTER);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);head.addView(title);TextView sub=tv("Segurança, controle e praticidade na estrada.",14,Color.WHITE);sub.setGravity(Gravity.CENTER);head.addView(sub);root.addView(head);scroll();content.addView(tv("Bem-vindo!",22,TEXT));content.addView(tv("Registre a inspeção do veículo, salve o histórico e gere o PDF.",15,MUTED));Button n=btn("✓  Novo Checklist",GREEN);n.setOnClickListener(v->showNew());content.addView(n,lp(1,12));Button h=btn("▣  Histórico",BLUE);h.setOnClickListener(v->showHistory());content.addView(h,lp(1,12));Button st=btn("⚙  Configurações",Color.rgb(92,111,128));st.setOnClickListener(v->showSettings());content.addView(st,lp(1,12));}
    LinearLayout.LayoutParams lp(int w,int top){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,58);p.topMargin=top;return p;}
    EditText field(String hint){EditText e=new EditText(this);e.setHint(hint);e.setTextSize(15);e.setSingleLine(true);GradientDrawable g=new GradientDrawable();g.setColor(Color.WHITE);g.setStroke(1,Color.rgb(210,220,230));g.setCornerRadius(14);e.setBackground(g);e.setPadding(16,0,16,0);content.addView(e,new LinearLayout.LayoutParams(-1,52));return e;}
    void showNew(){page();top("Novo Checklist");scroll();content.addView(tv("Dados do veículo",22,TEXT));content.addView(tv("Nome do motorista *",13,MUTED));driver=field("Ex.: João Silva");content.addView(tv("Data do dia *",13,MUTED));date=field("dd/MM/aaaa");date.setText(new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(new Date()));content.addView(tv("Tipo de carreta",13,MUTED));type=new Spinner(this);type.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"Rodotrem","Carreta LS"}));content.addView(type,new LinearLayout.LayoutParams(-1,52));content.addView(tv("Placa do caminhão trator *",13,MUTED));tractor=field("ABC-1234");content.addView(tv("Placa da carreta 1 *",13,MUTED));trailer1=field("DEF-5678");content.addView(tv("Placa da carreta 2 (Rodotrem)",13,MUTED));trailer2=field("GHI-9012");Button start=btn("Iniciar Checklist  →",GREEN);start.setOnClickListener(v->startChecklist());content.addView(start,lp(1,18));}
    boolean validBasic(){if(driver.getText().toString().trim().isEmpty()||tractor.getText().toString().trim().isEmpty()||trailer1.getText().toString().trim().isEmpty()){Toast.makeText(this,"Preencha os campos obrigatórios.",Toast.LENGTH_SHORT).show();return false;}if(type.getSelectedItemPosition()==0&&trailer2.getText().toString().trim().isEmpty()){Toast.makeText(this,"Informe a placa da carreta 2 para Rodotrem.",Toast.LENGTH_SHORT).show();return false;}return true;}
    void startChecklist(){if(!validBasic())return;showQuestions();}
    TextView section(String s){TextView t=tv(s,17,TEXT);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(0,16,0,8);return t;}
   void showQuestions(){
    page();
    top("Checklist de Segurança");
    scroll();

    TextView progress=tv("34 ITENS • RESPONDA TODOS",13,BLUE);
    progress.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    progress.setPadding(4,4,4,10);
    content.addView(progress);

    groups.clear();
    obs.clear();

    for(int i=0;i<34;i++){
        LinearLayout card=new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(16,14,16,14);

        GradientDrawable bg=new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setStroke(1,Color.rgb(225,232,238));
        bg.setCornerRadius(18);
        card.setBackground(bg);
        card.setElevation(3);

        TextView q=tv(
            String.format(Locale.getDefault(),"%02d  %s",i+1,qs[i]),
            16,
            TEXT
        );
        q.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        q.setPadding(0,0,0,6);
        card.addView(q);

        RadioGroup g=new RadioGroup(this);
        g.setOrientation(RadioGroup.VERTICAL);

        String[] options=
            types[i].equals("sim_nao")
                ? new String[]{"Sim","Não"}
            : types[i].equals("valvula")
                ? new String[]{"Aço Inox","Alumínio"}
            : types[i].equals("carregamento")
                ? new String[]{"Bottom","Top","Bottom/Top"}
            : new String[]{"Conforme","Não Conforme","Não Aplicável"};

        for(String o:options){
            RadioButton r=new RadioButton(this);
            r.setText(o);
            r.setTextSize(15);
            r.setTextColor(TEXT);
            r.setPadding(8,5,8,5);
            g.addView(r,new RadioGroup.LayoutParams(-1,50));
        }

        EditText e=new EditText(this);
        e.setHint("Observação (obrigatória se não conforme)");
        e.setMinLines(2);
        e.setGravity(Gravity.TOP);
        e.setVisibility(View.GONE);
        e.setPadding(12,8,12,8);

        card.addView(g);
        card.addView(e,new LinearLayout.LayoutParams(-1,78));

        g.setOnCheckedChangeListener((group,checked)->{
            if(checked!=-1){
                String val=((RadioButton)group.findViewById(checked))
                    .getText().toString();

                boolean show=
                    val.equals("Não") ||
                    val.equals("Não Conforme");

                e.setVisibility(show ? View.VISIBLE : View.GONE);

                if(!show){
                    e.setText("");
                }
            }
        });

        groups.add(g);
        obs.add(e);

        LinearLayout.LayoutParams cp=
            new LinearLayout.LayoutParams(-1,-2);
        cp.topMargin=10;
        content.addView(card,cp);
    }

    LinearLayout bottom=new LinearLayout(this);
    bottom.setPadding(14,8,14,8);
    bottom.setBackgroundColor(Color.WHITE);

    Button finish=btn("✓  FINALIZAR CHECKLIST",GREEN);
    finish.setTextSize(17);
    finish.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
    finish.setMinHeight(60);
    finish.setOnClickListener(v->finishChecklist());

    bottom.addView(
        finish,
        new LinearLayout.LayoutParams(-1,60)
    );

    root.addView(
        bottom,
        new LinearLayout.LayoutParams(-1,76)
    );
}
    void finishChecklist(){try{JSONObject a=new JSONObject();int conf=0,nc=0,na=0;for(int i=0;i<34;i++){int id=groups.get(i).getCheckedRadioButtonId();if(id==-1){Toast.makeText(this,"Responda o item "+(i+1)+".",Toast.LENGTH_SHORT).show();return;}String val=((RadioButton)groups.get(i).findViewById(id)).getText().toString();String ob=obs.get(i).getText().toString();if((val.equals("Não")||val.equals("Não Conforme"))&&ob.trim().isEmpty()){Toast.makeText(this,"Informe a observação do item "+(i+1)+".",Toast.LENGTH_SHORT).show();return;}a.put("q"+(i+1),val);a.put("o"+(i+1),ob);if(val.equals("Conforme")||val.equals("Sim"))conf++;else if(val.equals("Não Conforme")||val.equals("Não"))nc++;else if(val.equals("Não Aplicável"))na++;}ContentValues v=new ContentValues();v.put("created_at",new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(new Date()));v.put("driver",driver.getText().toString());v.put("date",date.getText().toString());v.put("type",type.getSelectedItem().toString());v.put("tractor",tractor.getText().toString());v.put("trailer1",trailer1.getText().toString());v.put("trailer2",trailer2.getText().toString());v.put("answers",a.toString());long id=db.insert(v);showDone(id,conf,nc,na);}catch(Exception e){Toast.makeText(this,"Erro ao salvar: "+e.getMessage(),Toast.LENGTH_LONG).show();}}
    void showDone(long id,int conf,int nc,int na){page();top("Checklist concluído");scroll();TextView ok=tv("✓",52,GREEN);ok.setGravity(Gravity.CENTER);content.addView(ok);TextView t=tv("CHECKLIST CONCLUÍDO!",23,GREEN);t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(t);content.addView(tv("Salvo no histórico deste aparelho.",15,MUTED));LinearLayout stats=new LinearLayout(this);stats.setGravity(Gravity.CENTER);stats.addView(stat(String.valueOf(conf),"Conformes",GREEN));stats.addView(stat(String.valueOf(nc),"Não conformes",RED));stats.addView(stat(String.valueOf(na),"N/A",MUTED));content.addView(stats,new LinearLayout.LayoutParams(-1,100));Button pdf=btn("▣  Gerar PDF",BLUE);pdf.setOnClickListener(v->makePdf(id));content.addView(pdf,lp(1,16));Button hist=btn("▤  Ver Histórico",NAVY);hist.setOnClickListener(v->showHistory());content.addView(hist,lp(1,10));Button home=btn("⌂  Início",Color.rgb(92,111,128));home.setOnClickListener(v->showHome());content.addView(home,lp(1,10));}
    TextView stat(String n,String l,int c){TextView t=tv(n+"\n"+l,18,c);t.setGravity(Gravity.CENTER);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setPadding(10,10,10,10);return t;}
    void showHistory(){page();top("Histórico");scroll();content.addView(tv("Checklists salvos neste aparelho",21,TEXT));android.database.Cursor c=db.all();if(c.getCount()==0){content.addView(tv("Nenhum checklist realizado ainda.",15,MUTED));return;}while(c.moveToNext()){final long id=c.getLong(c.getColumnIndexOrThrow("id"));LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(16,12,16,12);GradientDrawable bg=new GradientDrawable();bg.setColor(Color.WHITE);bg.setCornerRadius(16);bg.setStroke(1,Color.rgb(225,232,238));card.setBackground(bg);card.addView(tv(c.getString(c.getColumnIndexOrThrow("date"))+" • "+c.getString(c.getColumnIndexOrThrow("driver")),17,TEXT));card.addView(tv(c.getString(c.getColumnIndexOrThrow("type"))+" • "+c.getString(c.getColumnIndexOrThrow("tractor")),14,MUTED));Button b=btn("Abrir checklist",BLUE);b.setOnClickListener(v->showDetail(id));card.addView(b,new LinearLayout.LayoutParams(-1,50));content.addView(card,new LinearLayout.LayoutParams(-1,-2){{topMargin=10;}});}c.close();}
    void showDetail(long id){android.database.Cursor c=db.one(id);if(!c.moveToFirst()){c.close();return;}String driver=c.getString(c.getColumnIndexOrThrow("driver")),date=c.getString(c.getColumnIndexOrThrow("date")),typev=c.getString(c.getColumnIndexOrThrow("type")),tr=c.getString(c.getColumnIndexOrThrow("tractor")),t1=c.getString(c.getColumnIndexOrThrow("trailer1")),t2=c.getString(c.getColumnIndexOrThrow("trailer2")),ans=c.getString(c.getColumnIndexOrThrow("answers"));c.close();page();top("Detalhes do Checklist");scroll();content.addView(tv(driver+" • "+date,20,TEXT));content.addView(tv(typev+" • "+tr+" • "+t1+(t2.isEmpty()?"":" • "+t2),14,MUTED));try{JSONObject a=new JSONObject(ans);for(int i=0;i<34;i++){String val=a.optString("q"+(i+1));String ob=a.optString("o"+(i+1));content.addView(tv(String.format(Locale.getDefault(),"%02d. %s",i+1,qs[i]),15,TEXT));TextView r=tv("Resultado: "+val+(ob.isEmpty()?"":"\nObservação: "+ob),14,val.equals("Não Conforme")||val.equals("Não")?RED:GREEN);r.setPadding(10,4,10,10);content.addView(r);}}catch(Exception e){}Button pdf=btn("▣  Gerar PDF",BLUE);pdf.setOnClickListener(v->makePdf(id));content.addView(pdf,lp(1,12));}
    void showSettings(){page();top("Configurações");scroll();content.addView(tv("Configurações",23,TEXT));content.addView(tv("Versão 1.0 • Checklist Rodotrem / LS",15,MUTED));content.addView(tv("Os checklists ficam armazenados localmente no aparelho. O PDF pode ser compartilhado pelo Android.",15,TEXT));Button clear=btn("Apagar histórico",RED);clear.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Apagar histórico?").setMessage("Todos os checklists locais serão apagados.").setNegativeButton("Cancelar",null).setPositiveButton("Apagar",(d,w)->{getApplicationContext().deleteDatabase("checklists.db");db=new DatabaseHelper(this);showHistory();}).show());content.addView(clear,lp(1,18));}
    void makePdf(long id){try{android.database.Cursor c=db.one(id);if(!c.moveToFirst()){c.close();return;}String driver=c.getString(c.getColumnIndexOrThrow("driver")),date=c.getString(c.getColumnIndexOrThrow("date")),typev=c.getString(c.getColumnIndexOrThrow("type")),tr=c.getString(c.getColumnIndexOrThrow("tractor")),t1=c.getString(c.getColumnIndexOrThrow("trailer1")),t2=c.getString(c.getColumnIndexOrThrow("trailer2")),ans=c.getString(c.getColumnIndexOrThrow("answers"));c.close();JSONObject a=new JSONObject(ans);android.graphics.pdf.PdfDocument doc=new android.graphics.pdf.PdfDocument();int pageNo=1;android.graphics.pdf.PdfDocument.PageInfo info=new android.graphics.pdf.PdfDocument.PageInfo.Builder(595,842,pageNo).create();android.graphics.pdf.PdfDocument.Page p=doc.startPage(info);Canvas canvas=p.getCanvas();Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);paint.setColor(NAVY);paint.setTextSize(20);paint.setTypeface(Typeface.DEFAULT_BOLD);canvas.drawText("CHECKLIST DE VEÍCULO DE CARGA",40,50,paint);paint.setTextSize(13);paint.setColor(TEXT);canvas.drawText("RODOTREM / LS",40,72,paint);paint.setTypeface(Typeface.DEFAULT);canvas.drawText("Motorista: "+driver,40,100,paint);canvas.drawText("Data: "+date+"   Tipo: "+typev,40,120,paint);canvas.drawText("Placas: "+tr+" / "+t1+(t2.isEmpty()?"":" / "+t2),40,140,paint);int y=175;for(int i=0;i<34;i++){if(y>800){doc.finishPage(p);pageNo++;info=new android.graphics.pdf.PdfDocument.PageInfo.Builder(595,842,pageNo).create();p=doc.startPage(info);canvas=p.getCanvas();y=50;}paint.setTypeface(Typeface.DEFAULT_BOLD);paint.setTextSize(10);paint.setColor(TEXT);canvas.drawText((i+1)+". "+qs[i],40,y,paint);paint.setTypeface(Typeface.DEFAULT);String val=a.optString("q"+(i+1));String ob=a.optString("o"+(i+1));paint.setColor(val.equals("Não")||val.equals("Não Conforme")?RED:GREEN);canvas.drawText("Resultado: "+val,55,y+15,paint);if(!ob.isEmpty()){paint.setColor(TEXT);canvas.drawText("Obs.: "+ob,55,y+30,paint);y+=15;}y+=38;}doc.finishPage(p);File dir=new File(getCacheDir(),"pdf");dir.mkdirs();File file=new File(dir,"Checklist_"+date.replace('/','-')+".pdf");FileOutputStream out=new FileOutputStream(file);doc.writeTo(out);out.close();doc.close();Uri uri=FileProvider.getUriForFile(this,"br.com.checklistrodotrem.fileprovider",file);Intent share=new Intent(Intent.ACTION_SEND);share.setType("application/pdf");share.putExtra(Intent.EXTRA_STREAM,uri);share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);startActivity(Intent.createChooser(share,"Compartilhar PDF"));}catch(Exception e){Toast.makeText(this,"Não foi possível gerar o PDF: "+e.getMessage(),Toast.LENGTH_LONG).show();}}
}
