// import
// 必要なmoduleのimport
// k6は独自のJS runtimeを用いておりNode.jsには依存していない点に注意
import http from 'k6/http';
import { sleep } from 'k6';

// opption
// テストの細かい設定を行う
export const options = {
  iterations: 10,
};

// Defalut function
// テストのシナリオを記述する
export default function () {
  // Make a GET request to the target URL
  http.get('https://quickpizza.grafana.com');

  // Sleep for 1 second to simulate real-world usage
  sleep(1);
}

// Lifecycle operation
// テストの前後などに行う処理を記述する