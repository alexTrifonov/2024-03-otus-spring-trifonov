<script>
    export default {
        props: {
            itemData: {
                type: Array
            },
            fields: {
                type: Array
            }
        }
            
    }
</script>


<template>
    <table class="table">
        <thead>
            <tr>
                <th v-for="field in fields" :key="field">
                    {{ field }}
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in itemData" :key="item">
                <td v-for="field in fields" :key="field">
                    {{ item[field] }}
                </td>
            </tr>
        </tbody>
    </table>
</template>

<style scoped>
    .table {
            border: 1px solid steelblue;
            width: 300px;
            border-collapse: collapse;
        }

        .table tr td, th {
            padding: 5px;
            border: 1px solid steelblue;
        }

        .table td:last-child, td:first-child {
            width: 50px;
        }
</style>