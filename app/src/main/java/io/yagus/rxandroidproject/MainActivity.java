package io.yagus.rxandroidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 예제 1
        Function<String, Integer> ballToIndex = ball ->{
            switch(ball) {
                case "RED": return 1;
                case "YELLOW": return 2;
                case "GREEN": return 3;
                case  "BLUE": return 5;
                default: return -1;
            }
        };

        String[] balls = {"RED", "YELLOW", "GREEN", "BLUE"};
        Observable<Integer> source = Observable.fromArray(balls)
                .map(ballToIndex);
        source.subscribe(o->Log.d("ex1",o.toString()));

        // 예제 2
        Observable<Integer> source2 = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                (a, b) -> a+ b)
                .zipWith(Observable.just(1,2,3), (ab,c) -> ab+c);
        source2.subscribe(o->Log.d("ex2", o.toString()));

        // 예제 3
        String[] orgs = {"1", "3", "5"};
        Observable<String> source3 = Observable.fromArray(orgs)
                .zipWith(Observable.interval(100L, TimeUnit.MICROSECONDS), (a,b) -> a);

        source3.map(item -> "<<" + item + ">>")
                .subscribeOn(Schedulers.computation())
                .subscribe(o->Log.d("ex3",o.toString()));

        // 예제 4
        Iterable<String> samples = Arrays.asList("banana", "orange", "apple", "apple mango", "melon", "watermelon");

        Observable.fromIterable(samples)
                .filter(s-> s.contains("apple"))
                .first("Not found")
                .subscribe(o->Log.d("ex4",o.toString()));

        // 예제 5
        Observable<Integer> source4 = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                });
        source4.subscribe(o->Log.d("ex5",o.toString()));

        // 예제 6
        Integer[] arr = {100, 200, 300};
        Observable<Integer> source5 = Observable.fromArray(arr);
        source5.subscribe(o->Log.d("ex6",o.toString()));

        // 예제 7
        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("William");
        names.add("Bob");

        Observable<String> source6 = Observable.fromIterable(names);
        source6.subscribe(o->Log.d("ex7",o.toString()));


        // 예제 8
        Observable.just("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o->Log.d("ex8",o.toString()));

        // 예제 9
        Button button = (Button) findViewById( R.id.bt ) ;

        RxView.clicks( button )
                .subscribe(e -> {
                    Toast.makeText(this, "클릭!!", Toast.LENGTH_SHORT).show();
                });

        TextView tv = (TextView) findViewById(R.id.textView);

        // 예제 10
        Observable.just(tv.getText().toString())
                .map(s ->s+"Rx!")
                .subscribe(text -> tv.setText(text));

    }
}
