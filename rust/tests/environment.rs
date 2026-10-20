//! The environment comes from the key before PHOENIXPERPS_ENVIRONMENT (docs/BEHAVIOUR.md "Configuration"). A test
//! binary of its own: the only one that sets a variable of the process.

use phoenixperps::Client;

#[test]
fn the_key_before_the_variable() {
    // SAFETY: no other thread of this binary runs yet, so none reads the environment meanwhile
    unsafe { std::env::set_var("PHOENIXPERPS_ENVIRONMENT", "live") };
    let build = |key: String| Client::builder().api_key(key).base_url("http://127.0.0.1:9").build();
    assert_eq!("sandbox", build(format!("ppk_test_{}", "A".repeat(50))).unwrap().environment(), "the key's own");
    assert_eq!("live", build("a_key_of_no_environment".into()).unwrap().environment(), "else the variable");
    let e = Client::builder().api_key(format!("ppk_test_{}", "A".repeat(50))).environment("live").build().unwrap_err();
    assert_eq!("wrong_environment", e.code, "a client of the other environment still fails");
}
